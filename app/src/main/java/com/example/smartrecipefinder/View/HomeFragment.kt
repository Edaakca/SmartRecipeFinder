package com.example.smartrecipefinder.View

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.smartrecipefinder.Data.PopularRecipe
import com.example.smartrecipefinder.R
import com.example.smartrecipefinder.Room.RecipeEntity
import com.example.smartrecipefinder.databinding.FragmentHomeBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var carouselHandler: Handler? = null
    private var carouselRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allRecipes = loadRecipesFromAssets()
        val carouselRecipes = allRecipes.filter { !it.imageUrl.isNullOrEmpty() }.shuffled().take(4)
        val carouselImages = carouselRecipes.map { it.imageUrl!! }
        val carouselAdapter = CarouselAdapter(carouselImages)
        binding.viewPagerCarousel.adapter = carouselAdapter

        setupDots(carouselImages.size)

        if (carouselImages.isNotEmpty()) {
            carouselHandler = Handler(Looper.getMainLooper())
            carouselRunnable = object : Runnable {
                override fun run() {
                    val current = binding.viewPagerCarousel.currentItem
                    val next = (current + 1) % carouselImages.size
                    binding.viewPagerCarousel.setCurrentItem(next, true)
                    carouselHandler?.postDelayed(this, 5000)
                }
            }

            carouselHandler?.postDelayed(carouselRunnable!!, 5000)
        }

        carouselImages.forEach { url ->
            Glide.with(requireContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .preload()
        }

        val quickActions = listOf(
            "Bugün Ne Yesek?",
            "Hızlı Tarifler (15 dk)",
            "Vejetaryen Öneriler",
            "Tatlı Zamanı",
            "Çorba Tarifleri",
            "Türk Mutfağı"
        )

        binding.recyclerQuickActions.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.recyclerQuickActions.adapter = QuickActionAdapter(quickActions)
        val popularRecipes = allRecipes.filter { !it.imageUrl.isNullOrEmpty() }.shuffled().take(6).map { recipe ->
            PopularRecipe(
                name = recipe.title,
                time = "${recipe.prep_time} dk",
                imageUrl = recipe.imageUrl!!
            )
        }

        binding.recyclerPopularRecipes.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.recyclerPopularRecipes.adapter = PopularRecipeAdapter(popularRecipes)
    }


    private fun loadRecipesFromAssets(): List<RecipeEntity> {
        return try {
            val json = requireContext().assets.open("recipes.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<RecipeEntity>>() {}.type
            Gson().fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun setupDots(count: Int) {
        binding.dotsContainer.removeAllViews()

        if (count == 0) return

        for (i in 0 until count) {
            val dot = ImageView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 0, 8, 0)
                }
                setImageResource(if (i == 0) R.drawable.dot_selected else R.drawable.dot_indicator)
            }

            binding.dotsContainer.addView(dot)
        }

        binding.viewPagerCarousel.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in 0 until binding.dotsContainer.childCount) {
                    val dot = binding.dotsContainer.getChildAt(i) as ImageView
                    dot.setImageResource(if (i == position) R.drawable.dot_selected else R.drawable.dot_indicator)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        carouselHandler?.removeCallbacks(carouselRunnable!!)
        carouselHandler = null
        carouselRunnable = null
        _binding = null
    }
}