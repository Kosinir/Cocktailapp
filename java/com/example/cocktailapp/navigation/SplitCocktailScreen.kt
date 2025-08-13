package com.example.cocktailapp.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cocktailapp.navigation.CocktailListScreen
import com.example.cocktailapp.navigation.CocktailDetailScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text


@Composable
fun SplitCocktailScreen(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    var selectedId by remember { mutableStateOf<Int?>(null) }

    Row(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            CocktailListScreen(
                onItemClick = { id -> selectedId = id },
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
            )
        }

        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .padding(start = 8.dp)
        ) {
            if (selectedId != null) {
                CocktailDetailScreen(cocktailId = selectedId!!)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Wybierz koktajl z listy")
                }
            }
        }
    }
}
