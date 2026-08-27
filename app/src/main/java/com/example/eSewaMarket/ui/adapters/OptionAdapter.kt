package com.example.eSewaMarket.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eSewaMarket.databinding.ItemOptionBtnBinding

class OptionAdapter : RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {

    private val options = mutableListOf<String>()
    private var selectedPosition = RecyclerView.NO_POSITION

    inner class OptionViewHolder(
        val binding: ItemOptionBtnBinding
    ) : RecyclerView.ViewHolder(binding.root)

    fun submitList(list: List<String>) {
        options.clear()
        options.addAll(list)
        selectedPosition = RecyclerView.NO_POSITION
        notifyItemChanged(selectedPosition)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionViewHolder {
        val binding = ItemOptionBtnBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.binding.rbOption.text = options[position]

        holder.binding.rbOption.isChecked = position == selectedPosition

        holder.binding.rbOption.setOnClickListener {
            val previous = selectedPosition
            selectedPosition = holder.bindingAdapterPosition

            if (previous != RecyclerView.NO_POSITION) {
                notifyItemChanged(previous)
            }
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount() = options.size
}