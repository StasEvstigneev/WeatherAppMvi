package com.esy.weatherappmvi.presentation.favorite

import com.esy.weatherappmvi.domain.model.City
import kotlinx.coroutines.flow.StateFlow

interface FavoriteComponent {

    val model: StateFlow<FavoriteStore.State>

    fun clickSearch()

    fun clickAddFavorite()

    fun clickCityItem(city: City)

}