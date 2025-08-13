package com.example.cocktailapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "cocktails")
@TypeConverters(IngredientsConverter::class)
data class CocktailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val prepTimeMinutes: Int,
    val ingredients: List<String>,
    val instructions: String,
    val imageRes: String
)