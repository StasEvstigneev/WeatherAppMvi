package com.esy.weatherappmvi.data.repository

import com.esy.weatherappmvi.data.local.db.FavoriteCitiesDao
import com.esy.weatherappmvi.data.mapper.toEntity
import com.esy.weatherappmvi.data.mapper.toCityList
import com.esy.weatherappmvi.domain.model.City
import com.esy.weatherappmvi.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteCitiesDao: FavoriteCitiesDao //заменить на базу данных
) : FavoriteRepository {
    override val favoriteCities: Flow<List<City>>
        get() = favoriteCitiesDao.getFavoriteCities()
            .map { it.toCityList() }

    override fun observeIsFavorite(cityId: Int): Flow<Boolean> = favoriteCitiesDao
        .observeIsFavorite(cityId = cityId)

    override suspend fun addToFavorite(city: City) = favoriteCitiesDao
        .addToFavorite(cityEntity = city.toEntity())

    override suspend fun removeFromFavorite(cityId: Int) = favoriteCitiesDao
        .removeFromFavorite(cityId = cityId)
}