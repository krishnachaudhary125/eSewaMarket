package com.example.eSewaMarket.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.Faqs
import com.example.eSewaMarket.databinding.ItemFaqBinding

class FaqAdapter(
    private val onFaqClick: (Faqs) -> Unit
) : ListAdapter<Faqs, FaqAdapter.FaqViewHolder>(FaqDiffCallback()) {

    inner class FaqViewHolder(private val binding: ItemFaqBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(faq: Faqs) {
            binding.faqAnswer.text = faq.answer
            binding.faqQuestion.text = faq.question

            binding.faqAnswer.visibility =
                if (faq.isExpanded) View.VISIBLE else View.GONE

            binding.arrow.setImageResource(
                if (faq.isExpanded)
                    R.drawable.ic_arrow_up
                else
                    R.drawable.ic_arrow_down
            )

            binding.root.setOnClickListener {

                faq.isExpanded = !faq.isExpanded

                binding.faqAnswer.visibility =
                    if (faq.isExpanded) View.VISIBLE else View.GONE

                binding.arrow.setImageResource(
                    if (faq.isExpanded)
                        R.drawable.ic_arrow_up
                    else
                        R.drawable.ic_arrow_down
                )
                onFaqClick(faq)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val binding = ItemFaqBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FaqViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FaqDiffCallback : DiffUtil.ItemCallback<Faqs>() {
        override fun areItemsTheSame(oldItem: Faqs, newItem: Faqs) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Faqs, newItem: Faqs) =
            oldItem == newItem
    }
}