package com.example.smartrecipefinder.View

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartrecipefinder.databinding.ItemRecipeBinding
import com.example.smartrecipefinder.Model.RecipeItem

class RecipeListAdapter(private var items: List<RecipeItem>) :
    RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = items[position]
        holder.binding.textRecipeTitle.text = item.title

        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.binding.imageRecipe)
    }

    override fun getItemCount() = items.size

    fun updateList(newList: List<RecipeItem>) {
        items = newList
        notifyDataSetChanged()
    }
}
