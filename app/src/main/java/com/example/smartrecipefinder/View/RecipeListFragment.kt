package com.example.smartrecipefinder.View

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartrecipefinder.Data.RecipeRepository
import com.example.smartrecipefinder.Util.toSearchable
import com.example.smartrecipefinder.ViewModel.RecipeViewModel
import com.example.smartrecipefinder.ViewModel.RecipeViewModelFactory
import com.example.smartrecipefinder.databinding.FragmentRecipeListBinding
import kotlinx.coroutines.launch

class RecipeListFragment : Fragment() {
    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory(RecipeRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewRecipes.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recipes.collect { allRecipes ->
                val prefs = requireContext().getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)
                val saved = prefs.getString("saved_ingredients", "") ?: ""
                val selectedIngredients = if (saved.isNotBlank()) {
                    saved.split(",").map { it.trim() }.filter { it.isNotBlank() }
                } else emptyList()

                val filteredRecipes = if (selectedIngredients.isEmpty()) {
                    allRecipes
                } else {
                    allRecipes.filter { recipe ->
                        val searchableRecipe = recipe.ingredients.toSearchable()
                        selectedIngredients.all { ing ->
                            val searchableIng = ing.toSearchable()
                            searchableRecipe.contains(searchableIng)
                        }
                    }
                }

                binding.recyclerViewRecipes.adapter = RecipeAdapter(filteredRecipes)
                binding.progressBar.visibility = View.GONE
                binding.tvNoRecipe.text = if (selectedIngredients.isEmpty()) {
                    "Tüm Tarifler (${allRecipes.size} adet)"
                } else {
                    "Eşleşen Tarifler (${filteredRecipes.size} adet)"
                }

                binding.tvNoRecipe.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}