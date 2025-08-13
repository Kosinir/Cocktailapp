package com.example.cocktailapp

import android.app.Application
import com.example.cocktailapp.data.local.AppDatabase
import com.example.cocktailapp.data.local.AssetCocktailDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CocktailApp : Application() {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getInstance(this)
        val dataSource = AssetCocktailDataSource(this, db.cocktailDao())

        appScope.launch {
            dataSource.preloadIfEmpty()
        }
    }
}
