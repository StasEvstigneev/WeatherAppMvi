package com.esy.weatherappmvi.domain.usecase

import com.esy.weatherappmvi.domain.entity.City
import com.esy.weatherappmvi.domain.repository.FavoriteRepository
import javax.inject.Inject

class ChangeFavoriteStateUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    suspend fun addToFavorite(city: City) = repository.addToFavorite(city)

    suspend fun removeFromFavorite(cityId: Int) = repository.removeFromFavorite(cityId)

}