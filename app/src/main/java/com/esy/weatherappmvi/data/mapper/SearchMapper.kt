package com.esy.weatherappmvi.data.mapper

import com.esy.weatherappmvi.data.network.dto.CityDto
import com.esy.weatherappmvi.domain.model.City

fun City.toDto(): CityDto = CityDto(id, name, country)

fun CityDto.toCity(): City = City(id, name, country)