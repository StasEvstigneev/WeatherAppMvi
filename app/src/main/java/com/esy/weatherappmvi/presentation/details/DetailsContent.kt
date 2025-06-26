package com.esy.weatherappmvi.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.esy.weatherappmvi.R
import com.esy.weatherappmvi.domain.model.Forecast
import com.esy.weatherappmvi.domain.model.Weather
import com.esy.weatherappmvi.presentation.extensions.formattedFullDate
import com.esy.weatherappmvi.presentation.extensions.formattedShortDayOfWeek
import com.esy.weatherappmvi.presentation.extensions.tempToFormattedString
import com.esy.weatherappmvi.presentation.ui.theme.CardGradients

@Composable
fun DetailsContent(component: DetailsComponent) {

    val state by component.model.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CardGradients.gradients[1].primaryGradient),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        topBar = {
            TopBar(
                cityName = state.city.name,
                isCityFavorite = state.isFavorite,
                onBackClick = { component.clickBack() },
                onFavoriteClick = { component.clickChangeFavoriteStatus() }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.padding(padding)
        ) {
            when (val forecastState = state.forecastState) {
                DetailsStore.State.ForecastState.Error -> {
                    Error()
                }

                DetailsStore.State.ForecastState.Initial -> {
                    Initial()
                }

                is DetailsStore.State.ForecastState.Loaded -> {
                    Forecast(forecast = forecastState.forecast)
                }

                DetailsStore.State.ForecastState.Loading -> {
                    Loading()
                }
            }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    cityName: String,
    isCityFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(text = cityName) },
        colors = TopAppBarDefaults
            .topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            ),
        navigationIcon = {
            IconButton(
                onClick = { onBackClick() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = { onFavoriteClick() }) {
                val icon = if (isCityFavorite) {
                    Icons.Default.Star
                } else {
                    Icons.Outlined.Star
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
        }

    )

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Forecast(forecast: Forecast) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = forecast.currentWeather.conditionText,
            style = MaterialTheme.typography.titleLarge,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = forecast.currentWeather.tempC.tempToFormattedString(),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 70.sp)
            )
            GlideImage(
                modifier = Modifier.size(70.dp),
                model = forecast.currentWeather.conditionIcon,
                contentDescription = null
            )
            Text(
                text = forecast.currentWeather.date.formattedFullDate(),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))
        AnimatedUpcomingWeather(forecast.upcoming)
        Spacer(modifier = Modifier.weight(0.5f))

    }

}

@Composable
private fun UpcomingWeather(
    upcoming: List<Weather>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(
                alpha = 0.24f
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(R.string.upcoming_forecast),
                style = MaterialTheme.typography.headlineMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                upcoming.forEach {
                    DayWeatherCard(it)
                }

            }

        }
    }

}

@Composable
private fun AnimatedUpcomingWeather(upcoming: List<Weather>) {
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(animationSpec = tween(500)) + slideIn(
            animationSpec = tween(500),
            initialOffset = { IntOffset(x = 0, y = it.height) }
        )
    ) {
        UpcomingWeather(upcoming = upcoming)

    }

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RowScope.DayWeatherCard(weather: Weather) {
    Card(
        modifier = Modifier
            .height(128.dp)
            .weight(1f),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = weather.tempC.tempToFormattedString(),
                color = Color.Black
            )
            GlideImage(
                modifier = Modifier.size(48.dp),
                model = weather.conditionIcon,
                contentDescription = null
            )
            Text(
                text = weather.date.formattedShortDayOfWeek(),
                color = Color.Black
            )
        }

    }

}


@Composable
private fun Initial() {

}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = Color.White
        )
    }

}

@Composable
private fun Error() {

}