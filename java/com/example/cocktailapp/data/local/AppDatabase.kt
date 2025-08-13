package com.example.cocktailapp.data.local

import androidx.room.Database
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CocktailEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(IngredientsConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun cocktailDao(): CocktailDao
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "cocktails.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}