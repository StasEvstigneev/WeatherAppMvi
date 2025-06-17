package com.esy.weatherappmvi.data.mapper

import com.esy.weatherappmvi.data.network.dto.WeatherCurrentDto
import com.esy.weatherappmvi.data.network.dto.WeatherDto
import com.esy.weatherappmvi.data.network.dto.WeatherForecastDto
import com.esy.weatherappmvi.domain.model.Forecast
import com.esy.weatherappmvi.domain.model.Weather
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toWeather(): Weather = current.toWeather()

fun WeatherDto.toWeather(): Weather = Weather(
    tempC = tempC,
    conditionText = conditionDto.text,
    conditionIcon = conditionDto.iconUrl.correctIconUrl(),
    date = date.toCalendar()
)

fun WeatherForecastDto.toForecast() = Forecast(
    currentWeather = current.toWeather(),
    upcoming = forecastDto.forecastDay
        .drop(FIRST_ELEMENT)
        .map { dayDto ->
            val dayWeatherDto = dayDto.dayWeatherDto
            Weather(
                tempC = dayWeatherDto.tempC,
                conditionText = dayWeatherDto.conditionDto.text,
                conditionIcon = dayWeatherDto.conditionDto.iconUrl.correctIconUrl(),
                date = dayDto.date.toCalendar()
            )
        }
)

private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * RATIO_TO_MILLIS)
}

private fun String.correctIconUrl() =
    "https:$this".replace(
        oldValue = "64x64",
        newValue = "128x128"
    )

private const val FIRST_ELEMENT = 1
private const val RATIO_TO_MILLIS = 1000