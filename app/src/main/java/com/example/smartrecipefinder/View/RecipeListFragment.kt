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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartrecipefinder.R
import com.example.smartrecipefinder.Util.toSearchable
import com.example.smartrecipefinder.ViewModel.RecipeViewModel
import com.example.smartrecipefinder.ViewModel.RecipeViewModelFactory
import com.example.smartrecipefinder.databinding.FragmentRecipeListBinding
import com.example.smartrecipefinder.Data.RecipeRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecipeListFragment : Fragment() {

    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory(RecipeRepository(requireContext()))
    }

    private var currentSelectedIngredients: List<String> = emptyList()

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
            viewModel.recipes.collectLatest { allRecipes ->
                val prefs = requireContext().getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)
                val saved = prefs.getString("saved_ingredients", "") ?: ""
                currentSelectedIngredients = if (saved.isNotBlank()) {
                    saved.split(",").map { it.trim() }.filter { it.isNotBlank() }
                } else emptyList()

                val filteredRecipes = if (currentSelectedIngredients.isEmpty()) {
                    allRecipes
                } else {
                    allRecipes.filter { recipe ->
                        val searchableRecipe = recipe.ingredients.toSearchable()
                        currentSelectedIngredients.all { ing ->
                            val searchableIng = ing.toSearchable()
                            searchableRecipe.contains(searchableIng)
                        }
                    }
                }

                binding.recyclerViewRecipes.adapter = RecipeAdapter(filteredRecipes) { recipe ->
                    val bundle = Bundle().apply {
                        putParcelable("recipe", recipe)
                    }

                    findNavController().navigate(R.id.action_recipeListFragment_to_recipeDetailFragment, bundle)
                }

                binding.progressBar.visibility = View.GONE
                binding.tvNoRecipe.text = if (currentSelectedIngredients.isEmpty()) {
                    "Tüm Tarifler (${allRecipes.size} adet)"
                } else {
                    "Eşleşen Tarifler (${filteredRecipes.size} adet)"
                }

                binding.tvNoRecipe.visibility = View.VISIBLE
                updateClearFilterButton()
            }
        }

        binding.buttonClearFilter.setOnClickListener {
            Log.d("RecipeList", "Filtreyi Temizle butonuna TIKLANDI!")

            val prefs = requireContext().getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)
            prefs.edit().remove("saved_ingredients").apply()
            currentSelectedIngredients = emptyList()
            updateClearFilterButton()

            viewModel.recipes.value?.let { allRecipes ->
                Log.d("RecipeList", "Liste yenileniyor - tüm tarifler: ${allRecipes.size}")

                binding.recyclerViewRecipes.adapter = RecipeAdapter(allRecipes) { recipe ->
                    val bundle = Bundle().apply {
                        putParcelable("recipe", recipe)
                    }

                    findNavController().navigate(R.id.action_recipeListFragment_to_recipeDetailFragment, bundle)
                }

                binding.tvNoRecipe.text = "Tüm Tarifler (${allRecipes.size} adet)"
                binding.tvNoRecipe.visibility = View.VISIBLE
            } ?: run {
                Log.d("RecipeList", "recipes.value NULL - veri yüklenmedi")
            }
        }
    }

    private fun updateClearFilterButton() {
        Log.d("RecipeList", "Buton görünürlüğü kontrolü - malzeme sayısı: ${currentSelectedIngredients.size}")
        binding.buttonClearFilter?.visibility =
            if (currentSelectedIngredients.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}