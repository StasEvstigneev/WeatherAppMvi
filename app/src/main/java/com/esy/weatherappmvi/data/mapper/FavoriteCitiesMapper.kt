package com.esy.weatherappmvi.data.mapper

import com.esy.weatherappmvi.data.local.entity.CityEntity
import com.esy.weatherappmvi.domain.model.City

fun City.toEntity(): CityEntity = CityEntity(id, name, country)

fun CityEntity.toCity(): City = City(id, name, country)

fun List<CityEntity>.toCityList(): List<City> = map { it.toCity() }