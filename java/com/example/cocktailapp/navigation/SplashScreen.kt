package com.example.cocktailapp.navigation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cocktailapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeout: () -> Unit,
    durationMillis: Int = 3000
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 2000, easing = LinearEasing),
            RepeatMode.Restart
        )
    )

    LaunchedEffect(Unit) {
        delay(durationMillis.toLong())
        onTimeout()
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_cocktail_splash),
            contentDescription = "Splash Logo",
            modifier = Modifier
                .size(150.dp)
                .graphicsLayer { rotationZ = angle }
        )
    }
}
