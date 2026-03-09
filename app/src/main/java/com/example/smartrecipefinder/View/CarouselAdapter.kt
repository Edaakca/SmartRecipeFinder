package com.example.smartrecipefinder.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.smartrecipefinder.R

class CarouselAdapter(
    private val images: List<String>
) : RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageCarousel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carousel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = images.getOrNull(position) ?: return

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder_food)
            .error(R.drawable.placeholder_food)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .priority(Priority.HIGH)
            .dontAnimate()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = images.size

}