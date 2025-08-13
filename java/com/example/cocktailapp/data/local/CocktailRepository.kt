package com.example.cocktailapp.data.local

import com.example.cocktailapp.data.local.CocktailDao
import com.example.cocktailapp.data.local.CocktailEntity

class CocktailRepository (private val cocktailDao: CocktailDao) {
    suspend fun getCocktailById(id: Int): CocktailEntity? {
        return cocktailDao.getById(id)
    }

    suspend fun getAllCocktails(): List<CocktailEntity> {
        return cocktailDao.getAll()
    }

    suspend fun updateCocktail(cocktail: CocktailEntity) {
        cocktailDao.update(cocktail)
    }

}