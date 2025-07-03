package com.esy.weatherappmvi.presentation.favorite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.esy.weatherappmvi.R
import com.esy.weatherappmvi.presentation.extensions.tempToFormattedString
import com.esy.weatherappmvi.presentation.ui.theme.Error
import com.esy.weatherappmvi.presentation.ui.theme.Loading
import com.esy.weatherappmvi.presentation.ui.theme.Orange
import com.esy.weatherappmvi.presentation.ui.theme.TempGradient
import com.esy.weatherappmvi.presentation.ui.theme.TempGradients
import com.esy.weatherappmvi.presentation.ui.theme.getTempGradient

@Composable
fun FavoriteContent(component: FavoriteComponent) {

    val state by component.model.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                SearchCard(
                    onClick = { component.clickSearch() }
                )
            }
            items(
                items = state.cityItems,
                key = { it.city.id }
            ) { city ->
                CityCard(
                    cityItem = city,
                    onCityClick = { component.clickCityItem(city.city) }
                )
            }
            item {
                AddFavoriteCityCard(
                    onAddFavoriteClick = { component.clickAddFavorite() }
                )
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CityCard(
    cityItem: FavoriteStore.State.CityItem,
    onCityClick: () -> Unit
) {
    val gradient = when (val state = cityItem.weatherState) {
        is FavoriteStore.State.WeatherState.Loaded -> {
            getGradientByTemperature(state.tempC.toInt())
        }

        else -> {
            TempGradients.tempGradients[7]
        }
    }

    Card(
        modifier = Modifier
            .clickable {
                onCityClick()
            }
            .fillMaxSize()
            .shadow(
                shape = MaterialTheme.shapes.extraLarge,
                elevation = 16.dp,
                spotColor = gradient.shadowColor
            ),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = Color.Blue)
    ) {
        Box(
            modifier = Modifier
                .background(gradient.primaryGradient)
                .sizeIn(minHeight = 196.dp)
                .fillMaxWidth()
                .drawBehind {
                    drawCircle(
                        brush = gradient.secondaryGradient,
                        center = Offset(
                            x = center.x - size.width / 10,
                            y = center.y + size.height / 2
                        ),
                        radius = size.minDimension / 2
                    )
                }
                .padding(horizontal = 12.dp, vertical = 24.dp)
        ) {
            when (val weatherState = cityItem.weatherState) {
                FavoriteStore.State.WeatherState.Error -> {
                    Error()
                }

                FavoriteStore.State.WeatherState.Initial -> {}

                is FavoriteStore.State.WeatherState.Loaded -> {
                    GlideImage(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(56.dp),
                        model = weatherState.iconUrl,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 24.dp),
                        text = weatherState.tempC.tempToFormattedString(),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 48.sp),
                        overflow = TextOverflow.Ellipsis
                    )
                }

                FavoriteStore.State.WeatherState.Loading -> {
                    Loading()
                }
            }
            Text(
                modifier = Modifier.align(Alignment.BottomStart),
                text = cityItem.city.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}

@Composable
private fun AddFavoriteCityCard(
    onAddFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onAddFavoriteClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.onSurface)
    ) {
        Column(
            modifier = Modifier
                .sizeIn(minHeight = 196.dp)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .size(48.dp),
                imageVector = Icons.Default.Edit,
                tint = Orange,
                contentDescription = null
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(R.string.add_favorite),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun SearchCard(
    onClick: () -> Unit
) {
    val gradient = TempGradients.tempGradients[7]

    Card(
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier
                .clickable(enabled = true) { onClick() }
                .fillMaxWidth()
                .background(gradient.secondaryGradient),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(16.dp),
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = stringResource(R.string.search),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

private fun getGradientByTemperature(tempC: Int): TempGradient {
    return getTempGradient(tempC)
}