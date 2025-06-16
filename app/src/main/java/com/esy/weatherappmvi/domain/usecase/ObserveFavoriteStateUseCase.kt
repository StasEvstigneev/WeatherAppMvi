package com.esy.weatherappmvi.domain.usecase

import com.esy.weatherappmvi.domain.repository.FavoriteRepository
import javax.inject.Inject

class ObserveFavoriteStateUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(cityId: Int) = repository.observeIsFavorite(cityId)
}