package com.example.smartrecipefinder.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<RecipeEntity>)
    @Query("SELECT * FROM recipes")
    suspend fun getAll(): List<RecipeEntity>
}
