package com.esy.weatherappmvi.domain.repository

import com.esy.weatherappmvi.domain.model.City
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    val favoriteCities: Flow<List<City>>

    fun observeIsFavorite(cityId: Int): Flow<Boolean>

    suspend fun addToFavorite(city: City)

    suspend fun removeFromFavorite(cityId: Int)

}