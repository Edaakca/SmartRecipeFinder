package com.example.smartrecipefinder.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartrecipefinder.R

class IngredientsAdapter(
    private val ingredients: MutableList<String>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textIngredient: TextView = itemView.findViewById(R.id.textIngredient)
        val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.textIngredient.text = ingredients[position]
        holder.buttonDelete.setOnClickListener {
            onDeleteClick(position)


        }
    }

    override fun getItemCount(): Int = ingredients.size
}
