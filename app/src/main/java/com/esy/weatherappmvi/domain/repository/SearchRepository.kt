package com.esy.weatherappmvi.domain.repository

import com.esy.weatherappmvi.domain.entity.City

interface SearchRepository {

    suspend fun search(query: String): List<City>

}