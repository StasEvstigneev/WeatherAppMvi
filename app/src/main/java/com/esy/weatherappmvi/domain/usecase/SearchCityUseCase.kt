package com.esy.weatherappmvi.domain.usecase

import com.esy.weatherappmvi.domain.repository.SearchRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String) = repository.search(query)

}