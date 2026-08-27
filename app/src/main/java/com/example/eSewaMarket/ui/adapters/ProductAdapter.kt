package com.example.eSewaMarket.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.databinding.ItemProductBinding
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import kotlinx.coroutines.*

class ProductAdapter(
    private val cartViewModel: CartViewModel,
    private val favouriteViewModel: FavouriteViewModel,
    private val onClick: (Product) -> Unit,
    private val onAddToCartClick: (Product) -> Unit,
    private val onRemoveOneFromCartClick: (Long) -> Unit,
    private val onFavouriteClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

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

    override fun getItemCount() = productList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onViewRecycled(holder: ProductViewHolder) {
        super.onViewRecycled(holder)
        holder.quantityJob?.cancel()
        holder.favouriteJob?.cancel()
        holder.collapseJob?.cancel()
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.quantityJob?.cancel()
        holder.favouriteJob?.cancel()
        val product = productList[position]

        holder.binding.apply {

            productTitle.text = product.title
            brand.text = product.brand.uppercase()
            price.text = product.price.toString()
            if(product.stock != 0){
                soldOut.visibility = View.GONE
                addCartBtn.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(root.context, R.color.green)
                )
                plusProduct.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(root.context, R.color.white)
                )
                plusProduct.isEnabled = true
            }else{
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

            if (product.discountPercentage != null){
                val dis = product.discountPercentage.toInt()
                discount.text = root.context.getString(R.string.discount_off, dis)
                discount.visibility = View.VISIBLE
            }else{
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

    fun submitList(list: List<Product>) {
        differ.submitList(list)
    }
}