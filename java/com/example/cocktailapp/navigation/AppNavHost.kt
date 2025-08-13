package com.example.cocktailapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.cocktailapp.navigation.CocktailDetailScreen
import com.example.cocktailapp.navigation.CocktailListScreen
import com.example.cocktailapp.ui.utils.isTablet

@Composable
fun AppNavHost(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()
    val tablet = isTablet()

    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onTimeout = {
                navController.navigate("list"){
                    popUpTo("splash"){
                        inclusive = true}
                    }
                }
            )
        }

        composable("list") {
            if (tablet) {
                SplitCocktailScreen(
                    navController= navController,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = onToggleTheme
                )
            } else {
                CocktailListScreen(
                    onItemClick = { cocktailId ->
                        navController.navigate("detail/$cocktailId")
                    },
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = onToggleTheme
                )
            }
        }
        composable(
            "detail/{cocktailId}",
            arguments = listOf(navArgument("cocktailId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("cocktailId") ?: return@composable
            CocktailDetailScreen(cocktailId = id)
        }
    }
}

