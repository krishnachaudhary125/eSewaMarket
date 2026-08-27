package com.example.eSewaMarket.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eSewaMarket.databinding.ItemProductImageBinding

class ProductImageAdapter(
    private var images: List<String> = emptyList()
) : RecyclerView.Adapter<ProductImageAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(val binding: ItemProductImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemProductImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(holder.binding.root.context)
            .load(images[position])
            .into(holder.binding.productImage)
    }

    override fun getItemCount(): Int = images.size

    fun submitList(newImages: List<String>) {
        Log.d("ImageAdapter", "Images = $newImages")
        images = newImages
        notifyDataSetChanged()
    }
}