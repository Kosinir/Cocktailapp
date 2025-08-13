package com.example.cocktailapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDao {
    @Query("SELECT * FROM cocktails")
    suspend fun getAll(): List<CocktailEntity>

    @Query("SELECT * FROM cocktails WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): CocktailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cocktail: List<CocktailEntity>)

    @Update
    suspend fun update(cocktail: CocktailEntity)
}