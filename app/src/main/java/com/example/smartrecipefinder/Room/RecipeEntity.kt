package com.example.smartrecipefinder.Room

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val ingredients: String,
    val instructions: String,
    val prep_time: Int,
    val imageUrl: String? = null
) : android.os.Parcelable


