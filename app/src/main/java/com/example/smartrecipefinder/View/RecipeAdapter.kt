package com.example.smartrecipefinder.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.smartrecipefinder.R
import com.example.smartrecipefinder.Room.RecipeEntity

class RecipeAdapter(
    private val recipes: List<RecipeEntity>,
    private val onItemClick: (RecipeEntity) -> Unit = {}
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textRecipeTitle)
        val time: TextView = view.findViewById(R.id.textRecipeTime)
        val preview: TextView = view.findViewById(R.id.textIngredientsPreview)
        val imageRecipe: ImageView = view.findViewById(R.id.imageRecipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.title.text = recipe.title
        holder.time.text = "${recipe.prep_time} dk"
        val previewText = recipe.ingredients.split(",")
            .take(3)
            .joinToString(", ") + if (recipe.ingredients.split(",").size > 3) "..." else ""
        holder.preview.text = previewText.trim()

        Glide.with(holder.itemView.context)
            .load(recipe.imageUrl ?: "")
            .centerCrop()
            .placeholder(R.drawable.placeholder_food)
            .error(R.drawable.placeholder_food)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .into(holder.imageRecipe)

        holder.itemView.setOnClickListener {
            onItemClick(recipe)
        }
    }

    override fun getItemCount(): Int = recipes.size
}