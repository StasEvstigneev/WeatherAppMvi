package com.esy.weatherappmvi.presentation.details

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

class DefaultDetailsComponent @AssistedInject constructor(
    private val storeFactory: DetailsStoreFactory,
    @Assisted("city") private val city: City,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : DetailsComponent,
    ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(city = city) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    DetailsStore.Label.ClickBack -> {
                        onBackClicked()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State> = store.stateFlow

    override fun clickBack() {
        store.accept(DetailsStore.Intent.ClickBack)
    }

    override fun clickChangeFavoriteStatus() {
        store.accept(DetailsStore.Intent.ChangeFavoriteStatus)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("city") city: City,
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultDetailsComponent
    }
}