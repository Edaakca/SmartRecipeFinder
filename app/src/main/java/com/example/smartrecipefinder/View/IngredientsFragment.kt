package com.example.smartrecipefinder.View

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartrecipefinder.R
import com.example.smartrecipefinder.Util.toSearchable
import com.example.smartrecipefinder.databinding.FragmentIngredientsBinding
class IngredientsFragment : Fragment() {
    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!
    private val ingredients = mutableListOf<String>()
    private lateinit var adapter: IngredientsAdapter
    private val prefs by lazy {
        requireContext().getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = IngredientsAdapter(ingredients) { position ->
            if (position in ingredients.indices) {
                ingredients.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, ingredients.size)
                saveIngredients()
            }
            updateClearButtonVisibility()
            binding.buttonFindRecipe.visibility =
                if (ingredients.isNotEmpty()) View.VISIBLE else View.GONE
        }

        binding.recyclerViewIngredients.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewIngredients.adapter = adapter

        loadSavedIngredients()
        updateClearButtonVisibility()

        binding.buttonAddIngredient.setOnClickListener {
            val item = binding.editTextIngredient.text.toString().trim()
            if (item.isNotEmpty()) {
                val normalizedItem = item.toSearchable()
                ingredients.add(normalizedItem)
                adapter.notifyItemInserted(ingredients.size - 1)
                binding.editTextIngredient.text?.clear()
                binding.buttonFindRecipe.visibility = View.VISIBLE
                saveIngredients()
                updateClearButtonVisibility()
            }
        }

        binding.buttonFindRecipe.setOnClickListener {
            val ingredientString = ingredients.joinToString(",")
            val bundle = Bundle().apply {
                putString("ingredients", ingredientString)
            }
            findNavController().navigate(
                R.id.action_ingredientsFragment_to_recipeListFragment,
                bundle
            )
        }

        binding.buttonClearIngredients.setOnClickListener {
            ingredients.clear()
            adapter.notifyDataSetChanged()
            saveIngredients()
            binding.buttonFindRecipe.visibility = View.GONE
            binding.buttonClearIngredients.visibility = View.GONE
            Toast.makeText(requireContext(), "Malzemeler temizlendi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateClearButtonVisibility() {
        binding.buttonClearIngredients.visibility =
            if (ingredients.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun loadSavedIngredients() {
        val saved = prefs.getString("saved_ingredients", "") ?: ""
        if (saved.isNotBlank()) {
            ingredients.clear()
            ingredients.addAll(saved.split(",").map { it.trim() }.filter { it.isNotBlank() })
            adapter.notifyDataSetChanged()
            binding.buttonFindRecipe.visibility =
                if (ingredients.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun saveIngredients() {
        prefs.edit()
            .putString("saved_ingredients", ingredients.joinToString(","))
            .apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}