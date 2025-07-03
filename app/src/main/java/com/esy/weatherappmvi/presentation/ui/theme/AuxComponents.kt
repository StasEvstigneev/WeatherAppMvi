package com.esy.weatherappmvi.presentation.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Error() {
    Box(modifier = Modifier.fillMaxSize()) {
        Icon(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center)
                .size(48.dp),
            imageVector = Icons.Default.ErrorOutline,
            tint = Color.Red,
            contentDescription = null
        )
    }
}

@Composable
fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center),
            color = Color.Blue
        )
    }
}

@Composable
fun Initial() {}