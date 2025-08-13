package com.example.cocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cocktailapp.navigation.AppNavHost
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.windowsizeclass.*
import com.example.cocktailapp.navigation.HomeTabContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            val colors = if (isDarkTheme) darkColorScheme() else lightColorScheme()
            MaterialTheme(
                colorScheme = colors,
                typography = Typography(),
                content = {
                    AppNavHost(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = { isDarkTheme = !isDarkTheme }
                    )
                }
            )
        }
    }
}
