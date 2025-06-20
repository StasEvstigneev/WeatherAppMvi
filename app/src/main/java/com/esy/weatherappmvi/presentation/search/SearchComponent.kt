package com.esy.weatherappmvi.presentation.search

import com.esy.weatherappmvi.domain.model.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val model: StateFlow<SearchStore.State>

    fun clickBack()

    fun changeSearchQuery(query: String)

    fun onClickSearch()

    fun onClickCity(city: City)

}