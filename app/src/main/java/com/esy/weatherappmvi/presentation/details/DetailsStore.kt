package com.esy.weatherappmvi.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.esy.weatherappmvi.domain.model.City
import com.esy.weatherappmvi.domain.model.Forecast
import com.esy.weatherappmvi.domain.usecase.ChangeFavoriteStateUseCase
import com.esy.weatherappmvi.domain.usecase.GetForecastUseCase
import com.esy.weatherappmvi.domain.usecase.ObserveFavoriteStateUseCase
import com.esy.weatherappmvi.presentation.details.DetailsStore.Intent
import com.esy.weatherappmvi.presentation.details.DetailsStore.Label
import com.esy.weatherappmvi.presentation.details.DetailsStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

internal interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack : Intent
        data object ChangeFavoriteStatus : Intent
    }

    data class State(
        val city: City,
        val isFavorite: Boolean,
        val forecastState: ForecastState
    ) {

        sealed interface ForecastState {
            data object Initial : ForecastState
            data object Loading : ForecastState
            data object Error : ForecastState
            data class Loaded(val forecast: Forecast) : ForecastState
        }
    }

    sealed interface Label {
        data object ClickBack : Label
    }
}

internal class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getForecastUseCase: GetForecastUseCase,
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase,
    private val observeFavoriteStateUsecase: ObserveFavoriteStateUseCase
) {

    fun create(city: City): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                city = city,
                isFavorite = false,
                forecastState = State.ForecastState.Initial
            ),
            bootstrapper = BootstrapperImpl(city),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class FavoriteStatusChanged(val isFavorite: Boolean) : Action
        data class ForecastLoaded(val forecast: Forecast) : Action
        data object ForecastStartLoading : Action
        data object ForecastLoadingError : Action
    }

    private sealed interface Msg {
        data class FavoriteStatusChanged(val isFavorite: Boolean) : Msg
        data class ForecastLoaded(val forecast: Forecast) : Msg
        data object ForecastStartLoading : Msg
        data object ForecastLoadingError : Msg
    }

    private inner class BootstrapperImpl(val city: City) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                observeFavoriteStateUsecase(city.id).collect {
                    dispatch(Action.FavoriteStatusChanged(it))
                }
            }
            scope.launch {
                dispatch(Action.ForecastStartLoading)
                try {
                    val forecast = getForecastUseCase(city.id)
                    dispatch(Action.ForecastLoaded(forecast))
                } catch (e: Exception) {
                    dispatch(Action.ForecastLoadingError)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ChangeFavoriteStatus -> {
                    scope.launch {
                        val state = getState()
                        if (state.isFavorite) {
                            changeFavoriteStateUseCase.removeFromFavorite(state.city.id)
                        } else {
                            changeFavoriteStateUseCase.addToFavorite(state.city)
                        }
                    }
                }

                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavoriteStatusChanged -> {
                    dispatch(Msg.FavoriteStatusChanged(action.isFavorite))
                }

                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }

                Action.ForecastLoadingError -> {
                    dispatch(Msg.ForecastLoadingError)
                }

                Action.ForecastStartLoading -> {
                    dispatch(Msg.ForecastStartLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavoriteStatusChanged -> {
                copy(isFavorite = msg.isFavorite)
            }

            is Msg.ForecastLoaded -> {
                copy(forecastState = State.ForecastState.Loaded(msg.forecast))
            }

            Msg.ForecastLoadingError -> {
                copy(forecastState = State.ForecastState.Error)
            }

            Msg.ForecastStartLoading -> {
                copy(forecastState = State.ForecastState.Loading)
            }
        }
    }
}