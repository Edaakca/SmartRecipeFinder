package com.example.smartrecipefinder.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartrecipefinder.Data.RetrofitClient
import com.example.smartrecipefinder.Model.RecipeItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipeItem>>(emptyList())
    val recipes: StateFlow<List<RecipeItem>> = _recipes

    fun fetchRecipes(ingredients: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getRecipes(ingredients, apiKey = apiKey)
                _recipes.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
