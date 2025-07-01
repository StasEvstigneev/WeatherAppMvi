package com.esy.weatherappmvi.presentation.favorite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.esy.weatherappmvi.domain.model.City
import com.esy.weatherappmvi.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultFavoriteComponent @AssistedInject constructor(
    private val storeFactory: FavoriteStoreFactory,
    @Assisted("onAddToFavorite") private val onAddToFavorite: () -> Unit,
    @Assisted("onCityClicked") private val onCityClicked: (City) -> Unit,
    @Assisted("onSearchClicked") private val onSearchClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : FavoriteComponent,
    ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    FavoriteStore.Label.AddToFavorite -> {
                        onAddToFavorite()
                    }

                    is FavoriteStore.Label.CityItemClicked -> {
                        onCityClicked(it.city)
                    }

                    FavoriteStore.Label.ClickSearch -> {
                        onSearchClicked()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavoriteStore.State> = store.stateFlow

    override fun clickSearch() {
        store.accept(FavoriteStore.Intent.ClickSearch)
    }

    override fun clickAddFavorite() {
        store.accept(FavoriteStore.Intent.AddToFavorite)
    }

    override fun clickCityItem(city: City) {
        store.accept(FavoriteStore.Intent.CityItemClicked(city = city))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onAddToFavorite") onAddToFavorite: () -> Unit,
            @Assisted("onCityClicked") onCityClicked: (City) -> Unit,
            @Assisted("onSearchClicked") onSearchClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultFavoriteComponent
    }
}