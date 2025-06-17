package com.esy.weatherappmvi.di

import android.content.Context
import com.esy.weatherappmvi.data.local.db.FavoriteCitiesDao
import com.esy.weatherappmvi.data.local.db.FavoriteDatabase
import com.esy.weatherappmvi.data.network.api.ApiFactory
import com.esy.weatherappmvi.data.network.api.ApiService
import com.esy.weatherappmvi.data.repository.FavoriteRepositoryImpl
import com.esy.weatherappmvi.data.repository.SearchRepositoryImpl
import com.esy.weatherappmvi.data.repository.WeatherRepositoryImpl
import com.esy.weatherappmvi.domain.repository.FavoriteRepository
import com.esy.weatherappmvi.domain.repository.SearchRepository
import com.esy.weatherappmvi.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @[ApplicationScope Binds]
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @[ApplicationScope Binds]
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideApiService(): ApiService = ApiFactory.apiService

        @[ApplicationScope Provides]
        fun provideFavoriteDatabase(context: Context): FavoriteDatabase =
            FavoriteDatabase.getInstance(context)

        @[ApplicationScope Provides]
        fun provideFavoriteCitiesDao(database: FavoriteDatabase): FavoriteCitiesDao =
            database.favoriteCitiesDao()
    }

}