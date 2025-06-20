package com.esy.weatherappmvi.presentation.details

import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {

    val model: StateFlow<DetailsStore.State>

    fun clickBack()

    fun clickChangeFavoriteStatus()

}