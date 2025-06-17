package com.esy.weatherappmvi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cities")
data class CityEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String
)