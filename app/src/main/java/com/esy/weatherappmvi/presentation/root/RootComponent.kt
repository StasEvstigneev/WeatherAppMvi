package com.esy.weatherappmvi.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.esy.weatherappmvi.presentation.details.DetailsComponent
import com.esy.weatherappmvi.presentation.favorite.FavoriteComponent
import com.esy.weatherappmvi.presentation.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Favorite(val component: FavoriteComponent) : Child
        data class Search(val component: SearchComponent) : Child
        data class Details(val component: DetailsComponent) : Child
    }

}