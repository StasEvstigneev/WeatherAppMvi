package com.esy.weatherappmvi.domain.model

data class Forecast(
    val currentWeather: Weather,
    val upcoming: List<Weather>
)