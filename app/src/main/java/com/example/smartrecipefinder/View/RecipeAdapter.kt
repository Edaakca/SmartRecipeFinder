package com.example.smartrecipefinder.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartrecipefinder.R
import com.example.smartrecipefinder.Room.RecipeEntity
class RecipeAdapter(
    private val recipes: List<RecipeEntity>
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textRecipeTitle)
        val time: TextView = view.findViewById(R.id.textRecipeTime)
        val preview: TextView = view.findViewById(R.id.textIngredientsPreview)
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
        val previewText = recipe.ingredients.take(100) + if (recipe.ingredients.length > 100) "..." else ""
        holder.preview.text = previewText
    }


    override fun getItemCount(): Int = recipes.size
}
