package com.example.smartrecipefinder.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartrecipefinder.R
import com.example.smartrecipefinder.databinding.FragmentIngredientsBinding

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    private val ingredients = mutableListOf<String>()
    private lateinit var adapter: IngredientsAdapter

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
                adapter.notifyItemRangeChanged(position, ingredients.size - position)
            }

            binding.buttonFindRecipe.visibility =
                if (ingredients.isNotEmpty()) View.VISIBLE else View.GONE
        }

        binding.recyclerViewIngredients.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewIngredients.adapter = adapter

        binding.buttonAddIngredient.setOnClickListener {
            val item = binding.editTextIngredient.text.toString().trim()
            if (item.isNotEmpty()) {
                ingredients.add(item)
                adapter.notifyItemInserted(ingredients.size - 1)
                binding.editTextIngredient.text.clear()
                binding.buttonFindRecipe.visibility = View.VISIBLE
            }
        }

        // 🔹 Tarif bulma butonu
        binding.buttonFindRecipe.visibility = View.GONE
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
