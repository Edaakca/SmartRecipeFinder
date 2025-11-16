package com.example.smartrecipefinder.Data

import com.example.smartrecipefinder.Model.RecipeItem
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {

    @GET("recipes/findByIngredients")
    suspend fun getRecipes(
        @Query("ingredients") ingredients: String,
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String
    ): List<RecipeItem>
}
