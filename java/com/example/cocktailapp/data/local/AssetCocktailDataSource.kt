package com.example.cocktailapp.data.local

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AssetCocktailDataSource(
    private val context: Context,
    private val cocktailDao: CocktailDao
) {
    suspend fun preloadIfEmpty() = withContext(Dispatchers.IO) {
        if (cocktailDao.getAll().isNotEmpty()) return@withContext

        val json = context.assets.open("cocktails.json")
            .bufferedReader()
            .use { it.readText() }

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val type = Types.newParameterizedType(List::class.java, CocktailEntity::class.java)
        val adapter: JsonAdapter<List<CocktailEntity>> = moshi.adapter(type)
        val cocktails = adapter.fromJson(json).orEmpty()
        cocktailDao.insertAll(cocktails)

    }
}