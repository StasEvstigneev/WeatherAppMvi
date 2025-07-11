package com.esy.weatherappmvi.data.network.api

import com.esy.weatherappmvi.data.network.dto.CityDto
import com.esy.weatherappmvi.data.network.dto.WeatherCurrentDto
import com.esy.weatherappmvi.data.network.dto.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("current.json")
    suspend fun loadCurrentWeather(
        @Query("q") query: String
    ): WeatherCurrentDto

    @GET("forecast.json")
    suspend fun loadForecast(
        @Query("q") query: String,
        @Query("days") daysCount: Int = FORECAST_DAYS_COUNT
    ): WeatherForecastDto

    @GET("search.json")
    suspend fun searchCity(
        @Query("q") query: String
    ): List<CityDto>

    private companion object {
        private const val FORECAST_DAYS_COUNT = 4
    }

}