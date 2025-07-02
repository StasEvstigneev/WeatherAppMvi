package com.esy.weatherappmvi.presentation.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class TempGradient(
    val primaryGradient: Brush,
    val secondaryGradient: Brush,
    val shadowColor: Color
) {
    constructor(
        firstColor: Color,
        secondColor: Color,
        thirdColor: Color
    ) : this(
        primaryGradient = Brush.linearGradient(listOf(secondColor, thirdColor)),
        secondaryGradient = Brush.linearGradient(listOf(firstColor, thirdColor)),
        shadowColor = firstColor
    )

}

object TempGradients {
    val tempGradients = listOf(
        //SCORCHING
        TempGradient(
            firstColor = Color(0xFFFFF176),
            secondColor = Color(0xFFFF9100),
            thirdColor = Color(0xFFFF3D00)
        ),
        //HOT
        TempGradient(
            firstColor = Color(0xFFFFF9C4),
            secondColor = Color(0xFFFFCA28),
            thirdColor = Color(0xFFFF8F00)
        ),
        //WARM
        TempGradient(
            firstColor = Color(0xFFFFF3E0),
            secondColor = Color(0xFFFFE0B2),
            thirdColor = Color(0xFFFFD180)
        ),
        //MILD
        TempGradient(
            firstColor = Color(0xFFB2DFDB),
            secondColor = Color(0xFFFFF59D),
            thirdColor = Color(0xFFC5E1A5)
        ),
        //COOL
        TempGradient(
            firstColor = Color(0xFF64FFDA),
            secondColor = Color(0xFF80DEEA),
            thirdColor = Color(0xFFB2EBF2)
        ),
        //COLD
        TempGradient(
            firstColor = Color(0xFFE3F2FD),
            secondColor = Color(0xFF90CAF9),
            thirdColor = Color(0xFF64B5F6)
        ),
        //FREEZING
        TempGradient(
            firstColor = Color(0xFFB3E5FC),
            secondColor = Color(0xFF40C4FF),
            thirdColor = Color(0xFF2979FF)
        ),
        //DEFAULT
        TempGradient(
            firstColor = Color(0xFFE1F5FE),
            secondColor = Color(0xFFB3E5FC),
            thirdColor = Color(0xFF81D4FA)
        )
    )
}

enum class WeatherType {
    SCORCHING,
    HOT,
    WARM,
    MILD,
    COOL,
    COLD,
    FREEZING
}

private fun defineWeatherType(tempC: Int): WeatherType {
    return when (tempC) {
        in 33..Int.MAX_VALUE -> WeatherType.SCORCHING
        in 26..32 -> WeatherType.HOT
        in 21..25 -> WeatherType.WARM
        in 11..20 -> WeatherType.MILD
        in -5..10 -> WeatherType.COOL
        in -20..-6 -> WeatherType.COLD
        else -> WeatherType.FREEZING
    }
}

fun getTempGradient(tempC: Int): TempGradient {
    val weatherType = defineWeatherType(tempC)
    val gradients = TempGradients.tempGradients
    return when (weatherType) {
        WeatherType.SCORCHING -> gradients[0]
        WeatherType.HOT -> gradients[1]
        WeatherType.WARM -> gradients[2]
        WeatherType.MILD -> gradients[3]
        WeatherType.COOL -> gradients[4]
        WeatherType.COLD -> gradients[5]
        WeatherType.FREEZING -> gradients[6]
    }
}