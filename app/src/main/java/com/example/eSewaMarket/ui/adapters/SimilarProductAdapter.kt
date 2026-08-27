package com.example.eSewaMarket.ui.adapters

import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eSewaMarket.MainActivity
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.databinding.ItemProductBinding
import com.example.eSewaMarket.databinding.ItemSimilarHeaderBinding
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SimilarProductAdapter(
    private val cartViewModel: CartViewModel,
    private val favouriteViewModel: FavouriteViewModel,
    private val itemWidth: Int? = null,
    private val onClick: (Product) -> Unit,
    private val onAddToCartClick: (Product) -> Unit,
    private val onRemoveOneFromCartClick: (Long) -> Unit,
    private val onFavouriteClick: (Product) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_PRODUCT = 1
    }

    class HeaderViewHolder(
        val binding: ItemSimilarHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root)

    class SimilarProductViewHolder(
        val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        var quantityJob: Job? = null
        var favouriteJob: Job? = null
        var collapseJob: Job? = null

        var isQuantityExpanded = false
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var productList: List<Product>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HEADER else TYPE_PRODUCT
    }

    override fun getItemCount() = productList.size + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {

            TYPE_HEADER -> {
                HeaderViewHolder(ItemSimilarHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }

            else -> {
                SimilarProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is SimilarProductViewHolder) {
            holder.quantityJob?.cancel()
            holder.favouriteJob?.cancel()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.binding.shopNowBtn.setOnClickListener {
                    val intent = Intent(holder.itemView.context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        putExtra("open_fragment", "home")
                    }
                    holder.itemView.context.startActivity(intent)
                }
            }

            is SimilarProductViewHolder -> {
                itemWidth?.let {
                    holder.itemView.layoutParams.width = it
                }

                val product = productList[position - 1]

                holder.binding.apply {

                    productTitle.text = product.title
                    brand.text = product.brand.uppercase()
                    price.text = product.price.toString()
                    if (product.stock != 0) {
                        soldOut.visibility = View.GONE
                        addCartBtn.backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(root.context, R.color.green)
                        )
                        plusProduct.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(root.context, R.color.white)
                        )
                        plusProduct.isEnabled = true
                    } else {
                        soldOut.visibility = View.VISIBLE
                        addCartBtn.backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(root.context, R.color.addToCartSoldOut)
                        )
                        plusProduct.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(root.context, R.color.text_dark)
                        )
                        plusProduct.isEnabled = false
                    }

                    Glide.with(productImage.context)
                        .load(product.thumbnail)
                        .into(productImage)

                    if (product.discountPercentage != null) {
                        val dis = product.discountPercentage.toInt()
                        discount.text = "${dis}% OFF"
                        discount.visibility = View.VISIBLE
                    } else {
                        discount.visibility = View.GONE
                    }

                    imageContainer.setOnClickListener {
                        onClick(product)
                    }

                    plusProduct.setOnClickListener {
                        onAddToCartClick(product)

                        holder.isQuantityExpanded = true

                        holder.collapseJob?.cancel()

                        holder.collapseJob = holder.scope.launch {
                            delay(5_000)

                            holder.isQuantityExpanded = false

                            numOfProduct.visibility = View.GONE
                            minusProduct.visibility = View.GONE
                        }
                    }

                    minusProduct.setOnClickListener {
                        onRemoveOneFromCartClick(product.id)
                    }

                    holder.quantityJob?.cancel()
                    holder.quantityJob = holder.scope.launch {
                        cartViewModel.productQuantity(product.id).collect { qty ->

                            numOfProduct.text = qty.toString()

                            val visible = if (qty > 0 && holder.isQuantityExpanded) {
                                View.VISIBLE
                            } else {
                                View.GONE
                            }

                            numOfProduct.visibility = visible
                            minusProduct.visibility = visible
                        }
                    }

                    holder.favouriteJob?.cancel()
                    holder.favouriteJob = holder.scope.launch {
                        favouriteViewModel.isFavourite(product.id).collect { isFav ->
                            favourite.setImageResource(
                                if (isFav)
                                    R.drawable.ic_fav_filled
                                else
                                    R.drawable.ic_fav
                            )
                        }
                    }

                    favourite.setOnClickListener {
                        onFavouriteClick(product)
                    }
                }
            }
        }
    }

    fun submitList(list: List<Product>) {
        differ.submitList(list)
    }
}