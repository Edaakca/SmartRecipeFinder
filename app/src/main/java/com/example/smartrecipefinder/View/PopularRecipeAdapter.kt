package com.example.smartrecipefinder.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartrecipefinder.Data.PopularRecipe
import com.example.smartrecipefinder.R

class PopularRecipeAdapter(private val recipes: List<PopularRecipe>) :
    RecyclerView.Adapter<PopularRecipeAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageRecipe: ImageView = view.findViewById(R.id.imageRecipe)
        val textName: TextView = view.findViewById(R.id.textRecipeName)
        val textTime: TextView = view.findViewById(R.id.textRecipeTime)
        val imageHeart: ImageView = view.findViewById(R.id.imageHeart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_populer_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.textName.text = recipe.name
        holder.textTime.text = recipe.time

        Glide.with(holder.itemView.context)
            .load(recipe.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder_food)
            .error(R.drawable.placeholder_food)
            .into(holder.imageRecipe)

        holder.imageHeart.setOnClickListener {
            holder.imageHeart.setImageResource(R.drawable.ic_favorite)
        }
    }

    override fun getItemCount(): Int = recipes.size
}