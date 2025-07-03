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
        primaryGradient = Brush.linearGradient(listOf(firstColor, thirdColor)),
        secondaryGradient = Brush.linearGradient(listOf(secondColor, thirdColor)),
        shadowColor = firstColor
    )

}

object TempGradients {
    val tempGradients = listOf(
        //SCORCHING
        TempGradient(
            firstColor = Color(0xFFF57F17),
            secondColor = Color(0xFFE65100),
            thirdColor = Color(0xFFE53A07)
        ),
        //HOT
        TempGradient(
            firstColor = Color(0xFFFDD835),
            secondColor = Color(0xFFFB8C00),
            thirdColor = Color(0xFFFF9800)
        ),
        //WARM
        TempGradient(
            firstColor = Color(0xFFFFF3E0),
            secondColor = Color(0xFFFFEB3B),
            thirdColor = Color(0xFFFFB300)
        ),
        //MILD
        TempGradient(
            firstColor = Color(0xFFC5E1A5),
            secondColor = Color(0xFFFFEB3B),
            thirdColor = Color(0xFF388E3C)
        ),
        //COOL
        TempGradient(
            firstColor = Color(0xFF64FFDA),
            secondColor = Color(0xFF80DEEA),
            thirdColor = Color(0xFF29B6F6)
        ),
        //COLD
        TempGradient(
            firstColor = Color(0xFFE3F2FD),
            secondColor = Color(0xFF90CAF9),
            thirdColor = Color(0xFF29B6F6)
        ),
        //FREEZING
        TempGradient(
            firstColor = Color(0xFF81D4FA),
            secondColor = Color(0xFF40C4FF),
            thirdColor = Color(0xFF0D47A1)
        ),
        //DEFAULT
        TempGradient(
            firstColor = Color(0xFFE1F5FE),
            secondColor = Color(0xFF64B5F6),
            thirdColor = Color(0xFF03A9F4)
        ),
        //DEFAULT-LIGHT
        TempGradient(
            firstColor = Color(0xFFEFEFEF),
            secondColor = Color(0xFFFFFFFF),
            thirdColor = Color(0xFFFFFFFF)
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