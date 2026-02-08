package com.example.smartrecipefinder.Data

import android.content.Context
import com.example.smartrecipefinder.Room.AppDatabase
import com.example.smartrecipefinder.Room.RecipeEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class RecipeRepository(private val context: Context) {
    private val recipeDao = AppDatabase.getDatabase(context).recipeDao()
    suspend fun loadRecipesFromAssetsIfNeeded() {

        val count = recipeDao.getAll().size
        if (count > 0) return
        withContext(Dispatchers.IO) {
            try {
                val jsonString = readJsonFromAssets("recipes.json")
                val recipeListType = object : TypeToken<List<RecipeEntity>>() {}.type
                val recipes: List<RecipeEntity> = Gson().fromJson(jsonString, recipeListType)

                recipeDao.insertAll(recipes)
            } catch (e: Exception) {

            }
        }
    }

    private fun readJsonFromAssets(fileName: String): String {
        val inputStream = context.assets.open(fileName)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        return bufferedReader.use { it.readText() }
    }

    suspend fun getAllRecipes(): List<RecipeEntity> = recipeDao.getAll()

    suspend fun getFilteredRecipes(ingredients: List<String>): List<RecipeEntity> {
        val all = recipeDao.getAll()
        return all.filter { r ->
            ingredients.all { ing -> r.ingredients.contains(ing, ignoreCase = true) }
        }
    }
}