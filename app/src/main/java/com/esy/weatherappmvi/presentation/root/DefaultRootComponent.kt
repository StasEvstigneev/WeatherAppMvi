package com.esy.weatherappmvi.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.esy.weatherappmvi.domain.model.City
import com.esy.weatherappmvi.presentation.details.DefaultDetailsComponent
import com.esy.weatherappmvi.presentation.favorite.DefaultFavoriteComponent
import com.esy.weatherappmvi.presentation.search.DefaultSearchComponent
import com.esy.weatherappmvi.presentation.search.OpenReason
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val favoriteComponentFactory: DefaultFavoriteComponent.Factory,
    private val searchComponentFactory: DefaultSearchComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Favorite,
        handleBackButton = true,
        childFactory = ::child
    )

    @OptIn(DelicateDecomposeApi::class)
    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child {
        return when (config) {
            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    city = config.city,
                    onBackClicked = { navigation.pop() },
                    componentContext = componentContext
                )
                RootComponent.Child.Details(component = component)
            }

            Config.Favorite -> {
                val component = favoriteComponentFactory.create(
                    onAddToFavorite = {
                        navigation.push(Config.Search(openReason = OpenReason.AddToFavorite))
                    },
                    onCityClicked = {
                        navigation.push(Config.Details(it))
                    },
                    onSearchClicked = {
                        navigation.push(Config.Search(openReason = OpenReason.RegularSearch))
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Favorite(component = component)
            }

            is Config.Search -> {
                val component = searchComponentFactory.create(
                    openReason = config.openReason,
                    onBackClicked = {
                        navigation.pop()
                    },
                    onOpenForecast = {
                        navigation.push(Config.Details(it))
                    },
                    onFavoriteSaved = {
                        navigation.pop()
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Search(component = component)
            }
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Favorite : Config

        @Serializable
        data class Search(val openReason: OpenReason) : Config

        @Serializable
        data class Details(val city: City) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}