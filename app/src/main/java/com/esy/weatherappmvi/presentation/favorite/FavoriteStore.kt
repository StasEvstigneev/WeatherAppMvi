package com.esy.weatherappmvi.presentation.favorite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.esy.weatherappmvi.domain.model.City
import com.esy.weatherappmvi.domain.usecase.GetCurrentWeatherUseCase
import com.esy.weatherappmvi.domain.usecase.GetFavoriteCitiesUseCase
import com.esy.weatherappmvi.presentation.favorite.FavoriteStore.Intent
import com.esy.weatherappmvi.presentation.favorite.FavoriteStore.Label
import com.esy.weatherappmvi.presentation.favorite.FavoriteStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavoriteStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickSearch : Intent
        data object AddToFavorite : Intent
        data class CityItemClicked(val city: City) : Intent
    }

    data class State(
        val cityItems: List<CityItem>
    ) {

        data class CityItem(
            val city: City,
            val weatherState: WeatherState
        )

        sealed interface WeatherState {
            data object Initial : WeatherState
            data object Loading : WeatherState
            data object Error : WeatherState
            data class Loaded(
                val tempC: Float,
                val iconUrl: String
            ) : WeatherState
        }
    }

    sealed interface Label {
        data object ClickSearch : Label
        data object AddToFavorite : Label
        data class CityItemClicked(val city: City) : Label
    }
}

class FavoriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) {

    fun create(): FavoriteStore =
        object : FavoriteStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FavoriteStore",
            initialState = State(emptyList()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class FavoriteCitiesLoaded(val cities: List<City>) : Action
    }

    private sealed interface Msg {
        data class FavoriteCitiesLoaded(val cities: List<City>) : Msg

        data class WeatherLoaded(
            val cityId: Int,
            val tempC: Float,
            val iconUrl: String
        ) : Msg

        data class WeatherLoadingError(val cityId: Int) : Msg

        data class WeatherIsLoading(val cityId: Int) : Msg

    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavoriteCitiesUseCase().collect {
                    dispatch(Action.FavoriteCitiesLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddToFavorite -> {
                    publish(Label.AddToFavorite)
                }

                is Intent.CityItemClicked -> {
                    publish(Label.CityItemClicked(intent.city))
                }

                Intent.ClickSearch -> {
                    publish(Label.ClickSearch)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavoriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavoriteCitiesLoaded(cities))
                    cities.forEach {
                        scope.launch {
                            loadWeatherForCity(it)
                        }
                    }
                }
            }
        }

        private suspend fun loadWeatherForCity(city: City) {
            dispatch(Msg.WeatherIsLoading(city.id))
            try {
                val weather = getCurrentWeatherUseCase(cityId = city.id)
                dispatch(
                    Msg.WeatherLoaded(
                        cityId = city.id,
                        tempC = weather.tempC,
                        iconUrl = weather.conditionIcon
                    )
                )
            } catch (e: Exception) {
                dispatch(Msg.WeatherLoadingError(city.id))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavoriteCitiesLoaded -> {
                copy(
                    cityItems = msg.cities
                        .map {
                            State.CityItem(
                                city = it,
                                weatherState = State.WeatherState.Initial
                            )
                        }
                )
            }

            is Msg.WeatherIsLoading -> {
                copy(
                    cityItems = cityItems
                        .map {
                            if (it.city.id == msg.cityId) {
                                it.copy(weatherState = State.WeatherState.Loading)
                            } else {
                                it
                            }
                        }
                )
            }

            is Msg.WeatherLoaded -> {
                copy(
                    cityItems = cityItems
                        .map {
                            if (it.city.id == msg.cityId) {
                                it.copy(
                                    weatherState = State.WeatherState
                                        .Loaded(
                                            tempC = msg.tempC,
                                            iconUrl = msg.iconUrl
                                        )
                                )
                            } else {
                                it
                            }
                        }
                )
            }

            is Msg.WeatherLoadingError -> {
                copy(
                    cityItems = cityItems
                        .map {
                            if (it.city.id == msg.cityId) {
                                it.copy(weatherState = State.WeatherState.Error)
                            } else {
                                it
                            }
                        }
                )
            }
        }
    }
}