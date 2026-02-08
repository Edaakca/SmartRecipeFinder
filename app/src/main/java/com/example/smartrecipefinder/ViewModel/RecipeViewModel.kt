package com.example.smartrecipefinder.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartrecipefinder.Data.RecipeRepository
import com.example.smartrecipefinder.Room.RecipeEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipeEntity>>(emptyList())
    val recipes: StateFlow<List<RecipeEntity>> = _recipes.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _recipes.value = repository.getAllRecipes()
            } catch (e: Exception) {
                _recipes.value = emptyList()
            }
        }
    }

    suspend fun getFilteredRecipes(ingredients: List<String>): List<RecipeEntity> {
        return repository.getFilteredRecipes(ingredients)
    }
}
