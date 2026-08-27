package com.example.eSewaMarket.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.eSewaMarket.data.models.ProductResponse
import com.example.eSewaMarket.databinding.ItemCartProductBinding

class CartProductAdapter(
    private val onClick: (ProductResponse) -> Unit,
    private val onAddToCartClick: (Long) -> Unit,
    private val onRemoveOneFromCartClick: (Long) -> Unit
) : RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>() {

    class CartProductViewHolder(val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<ProductResponse>(){
        override fun areItemsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var productList: List<ProductResponse>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(
            ItemCartProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val product = productList[position]
        val quantity = product.quantity.toDouble()
        val price = product.price * quantity

        holder.binding.apply {
            Glide.with(productImage.context)
                .load(product.thumbnail)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(productImage)

            productName.text = product.title
            productBrand.text = product.brand
            productPrice.text = "%.2f".format(price)
            numOfProduct.text = product.quantity.toString()

            productLayout.setOnClickListener {
                onClick(product)
            }

            plusProduct.setOnClickListener {
                onAddToCartClick(product.productId)
            }

            minusProduct.setOnClickListener {
                onRemoveOneFromCartClick(product.productId)
            }
        }
    }

    override fun getItemCount() = productList.size

    fun submitList(list: List<ProductResponse>) {
        differ.submitList(list)
    }
}