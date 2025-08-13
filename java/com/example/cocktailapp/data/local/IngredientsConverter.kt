package com.example.cocktailapp.data.local

import androidx.room.TypeConverter

class IngredientsConverter {
    @TypeConverter
    fun fromIngredientsList(ingredients: List<String>): String {
        return ingredients.joinToString(separator = "||")
    }
    @TypeConverter
    fun ToList(data: String): List<String> {
        return if (data.isEmpty()) emptyList()
        else data.split("||")
    }
}