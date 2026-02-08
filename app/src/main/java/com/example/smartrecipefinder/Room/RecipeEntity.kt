package com.example.smartrecipefinder.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val ingredients: String,
    val instructions: String,
    val prep_time: Int
)

