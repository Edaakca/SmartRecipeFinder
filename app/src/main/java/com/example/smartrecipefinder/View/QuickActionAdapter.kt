package com.example.smartrecipefinder.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartrecipefinder.R

class QuickActionAdapter(private val actions: List<String>) :
    RecyclerView.Adapter<QuickActionAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textAction: TextView = view.findViewById(R.id.textAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quick_action, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textAction.text = actions[position]
    }

    override fun getItemCount(): Int = actions.size
}