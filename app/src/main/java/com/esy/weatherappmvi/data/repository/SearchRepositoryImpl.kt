package com.esy.weatherappmvi.data.repository

import com.esy.weatherappmvi.data.mapper.toCity
import com.esy.weatherappmvi.data.network.api.ApiService
import com.esy.weatherappmvi.domain.model.City
import com.esy.weatherappmvi.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {
    override suspend fun search(query: String): List<City> {
        return apiService.searchCity(query = query)
            .map { it.toCity() }
    }
}