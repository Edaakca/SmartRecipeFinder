package com.example.smartrecipefinder.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.smartrecipefinder.R
import com.example.smartrecipefinder.Room.RecipeEntity
import com.example.smartrecipefinder.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    // Favori durumu deneme için
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe: RecipeEntity? = arguments?.getParcelable("recipe")

        recipe?.let { r ->
            binding.textTitle.text = r.title
            binding.textTime.text = "${r.prep_time} dk"
            binding.textIngredients.text = r.ingredients
                .split(",")
                .joinToString("\n• ") { it.trim() }
            binding.textInstructions.text = r.instructions

            Glide.with(requireContext())
                .load(r.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder_food)
                .error(R.drawable.placeholder_food)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageRecipeDetail)

            updateFavoriteIcon()

            binding.imageFavorite.setOnClickListener {
                isFavorite = !isFavorite
                updateFavoriteIcon()
                val message = if (isFavorite) "Favorilere eklendi ❤️" else "Favorilerden çıkarıldı"
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            }
        } ?: run {
            Toast.makeText(requireContext(), "Tarif yüklenemedi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFavoriteIcon() {
        binding.imageFavorite.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_border
            else R.drawable.ic_favorite
        )

        binding.imageFavorite.setColorFilter(
            resources.getColor(R.color.accent, null)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}