package com.example.smartrecipefinder.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartrecipefinder.ViewModel.RecipeViewModel
import com.example.smartrecipefinder.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment() {

    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RecipeViewModel>()
    private lateinit var adapter: RecipeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ingredientsString = arguments?.getString("ingredients") ?: ""

        adapter = RecipeListAdapter(emptyList())
        binding.recyclerViewRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewRecipes.adapter = adapter

        val apiKey = "BURAYA_API_KEY"

        viewModel.fetchRecipes(ingredientsString, apiKey)

        lifecycleScope.launchWhenStarted {
            viewModel.recipes.collect { list ->
                adapter.updateList(list)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
