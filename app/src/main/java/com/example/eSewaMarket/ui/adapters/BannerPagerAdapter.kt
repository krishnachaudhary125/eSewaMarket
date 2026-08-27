package com.example.eSewaMarket.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.Banner
import com.example.eSewaMarket.databinding.ItemBannerBinding

class BannerPagerAdapter(
    private val onBannerClick: (Banner) -> Unit
) : ListAdapter<Banner, BannerPagerAdapter.BannerViewHolder>(BannerDiffCallback()) {

    inner class BannerViewHolder(
        private val binding: ItemBannerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(banner: Banner) {

            Glide.with(binding.bannerImage.context)
                .load(banner.imageUrl)
                .placeholder(R.drawable.banner1)
                .centerCrop()
                .into(binding.bannerImage)

            binding.root.setOnClickListener {
                onBannerClick(banner)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BannerViewHolder {

        val binding = ItemBannerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BannerViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class BannerDiffCallback : DiffUtil.ItemCallback<Banner>() {

        override fun areItemsTheSame(
            oldItem: Banner,
            newItem: Banner
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Banner,
            newItem: Banner
        ): Boolean {
            return oldItem == newItem
        }
    }
}