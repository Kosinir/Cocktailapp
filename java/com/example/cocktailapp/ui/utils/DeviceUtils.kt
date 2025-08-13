package com.example.cocktailapp.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun isTablet(): Boolean {
    val config = LocalConfiguration.current
    return config.screenWidthDp >= 600
}