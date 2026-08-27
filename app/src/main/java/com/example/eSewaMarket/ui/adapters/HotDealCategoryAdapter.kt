package com.example.eSewaMarket.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eSewaMarket.data.models.HotDeal
import com.example.eSewaMarket.databinding.ItemHotdealTextBinding

class HotDealCategoryAdapter(
    private val onClick: (HotDeal) -> Unit
) : ListAdapter<HotDeal, HotDealCategoryAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(val binding: ItemHotdealTextBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHotdealTextBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)

        holder.binding.btnCategory.text = category.name

        holder.binding.btnCategory.setOnClickListener {
            onClick(category)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HotDeal>() {
        override fun areItemsTheSame(oldItem: HotDeal, newItem: HotDeal): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: HotDeal, newItem: HotDeal): Boolean {
            return oldItem == newItem
        }
    }
}