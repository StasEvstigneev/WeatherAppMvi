package com.esy.weatherappmvi.domain.model

import java.util.Calendar

data class Weather(
    val tempC: Float,
    val conditionText: String,
    val conditionIcon: String, // еще есть вариант назвать conditionUrl
    val date: Calendar
)