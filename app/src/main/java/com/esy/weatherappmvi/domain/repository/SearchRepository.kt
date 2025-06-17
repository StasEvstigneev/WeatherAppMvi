package com.esy.weatherappmvi.domain.repository

import com.esy.weatherappmvi.domain.model.City

interface SearchRepository {

    suspend fun search(query: String): List<City>

}