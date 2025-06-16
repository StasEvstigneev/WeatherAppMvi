package com.esy.weatherappmvi.domain.usecase

import com.esy.weatherappmvi.domain.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoriteCitiesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke() = repository.favoriteCities

}