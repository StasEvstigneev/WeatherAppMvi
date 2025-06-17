package com.esy.weatherappmvi.domain.repository

import com.esy.weatherappmvi.domain.model.Forecast
import com.esy.weatherappmvi.domain.model.Weather

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather

    suspend fun getForecast(cityId: Int): Forecast

}