package com.esy.weatherappmvi.data.repository

import com.esy.weatherappmvi.data.mapper.toForecast
import com.esy.weatherappmvi.data.mapper.toWeather
import com.esy.weatherappmvi.data.network.api.ApiService
import com.esy.weatherappmvi.domain.model.Forecast
import com.esy.weatherappmvi.domain.model.Weather
import com.esy.weatherappmvi.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WeatherRepository {
    override suspend fun getWeather(cityId: Int): Weather {
        return apiService.loadCurrentWeather("$PREFIX_CITY_ID$cityId").toWeather()
    }

    override suspend fun getForecast(cityId: Int): Forecast {
        return apiService.loadForecast("$PREFIX_CITY_ID$cityId").toForecast()
    }

    private companion object {
        private const val PREFIX_CITY_ID = "id:"
    }

}