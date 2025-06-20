package com.esy.weatherappmvi.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.esy.weatherappmvi.presentation.details.DetailsContent
import com.esy.weatherappmvi.presentation.favorite.FavoriteContent
import com.esy.weatherappmvi.presentation.search.SearchContent
import com.esy.weatherappmvi.presentation.ui.theme.WeatherAppMviTheme

@Composable
fun RootContent(component: RootComponent) {
    WeatherAppMviTheme {
        Children(
            stack = component.stack
        ) {
            when (val instance = it.instance) {
                is RootComponent.Child.Details -> {
                    DetailsContent(component = instance.component)
                }

                is RootComponent.Child.Favorite -> {
                    FavoriteContent(component = instance.component)
                }

                is RootComponent.Child.Search -> {
                    SearchContent(component = instance.component)
                }
            }

        }
    }

}