package com.esy.weatherappmvi.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cities")
data class CityEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String
)