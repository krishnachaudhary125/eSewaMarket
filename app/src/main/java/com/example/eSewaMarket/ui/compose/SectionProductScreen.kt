package com.example.eSewaMarket.ui.compose

import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.databinding.ItemLoadingBinding
import com.example.eSewaMarket.databinding.ItemProductBinding
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import com.example.eSewaMarket.ui.viewmodel.SectionProductViewModel
import kotlinx.coroutines.delay

@Composable
fun SectionProductScreen(
    viewModel: SectionProductViewModel,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel,
    onClick: (Product) -> Unit,
    onAddToCartClick: (Product) -> Unit,
    onRemoveOneFromCart: (Product) -> Unit,
    onFavouriteClick: (Product) -> Unit
) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState, products.size) {
        snapshotFlow {
            gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collect { lastVisibleIndex ->

            if (
                products.isNotEmpty() &&
                lastVisibleIndex != null &&
                lastVisibleIndex >= products.size - 4
            ) {
                viewModel.loadNextPage()
            }
        }
    }

    when {
        isLoading && products.isEmpty() -> {
            LoadingItem()
        }

        error && products.isEmpty() -> {
            SectionProductError(
                onRetry = {
                    viewModel.retry()
                }
            )
        }

        products.isEmpty() -> {
            EmptySectionProduct()
        }

        else -> {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(
                    items = products,
                    key = { it.id }
                ) { product ->

                    ItemProductCard(
                        product = product,
                        cartViewModel = cartViewModel,
                        favouriteViewModel = favouriteViewModel,
                        onClick = onClick,
                        onAddToCartClick = onAddToCartClick,
                        onRemoveOneFromCart = onRemoveOneFromCart,
                        onFavouriteClick = onFavouriteClick
                    )
                }

                if (isLoading && products.isNotEmpty()) {

                    item(
                        span = {
                            GridItemSpan(maxLineSpan)
                        }
                    ) {
                        LoadingItem()
                    }
                }
            }
        }
    }
}

@Composable
fun ItemProductCard(
    product: Product,
    cartViewModel: CartViewModel,
    onClick: (Product) -> Unit,
    favouriteViewModel: FavouriteViewModel,
    onAddToCartClick: (Product) -> Unit,
    onRemoveOneFromCart: (Product) -> Unit,
    onFavouriteClick: (Product) -> Unit
) {
    val quantity by cartViewModel
        .productQuantity(product.id)
        .collectAsState(initial = 0)

    val isFavourite by favouriteViewModel
        .isFavourite(product.id)
        .collectAsState(initial = false)

    var isQuantityExpanded by remember(product.id) {
        mutableStateOf(false)
    }

    var expandRequest by remember(product.id) {
        mutableIntStateOf(0)
    }

    LaunchedEffect(expandRequest) {
        if (expandRequest > 0) {
            delay(5_000)
            isQuantityExpanded = false
        }
    }

    AndroidView(
        factory = { context ->
            ItemProductBinding.inflate(
                LayoutInflater.from(context)
            ).root
        },
        update = { view ->
            val binding = ItemProductBinding.bind(view)

            binding.apply {
                productTitle.text = product.title
                brand.text = product.brand
                price.text = product.price.toString()

                Glide.with(productImage.context)
                    .load(product.thumbnail)
                    .into(productImage)

                numOfProduct.text = quantity.toString()
                val visibility =
                    if (quantity > 0 && isQuantityExpanded) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                numOfProduct.visibility = visibility
                minusProduct.visibility = visibility

                favourite.setImageResource(
                    if (isFavourite)
                        R.drawable.ic_fav_filled
                    else
                        R.drawable.ic_fav
                )

                imageContainer.setOnClickListener {
                    onClick(product)
                }

                plusProduct.setOnClickListener {
                    isQuantityExpanded = true
                    expandRequest++
                    onAddToCartClick(product)
                }

                minusProduct.setOnClickListener {
                    onRemoveOneFromCart(product)
                }

                favourite.setOnClickListener {
                    onFavouriteClick(product)
                }
            }
        }
    )
}

@Composable
fun LoadingItem() {
    AndroidView(
        factory = { context ->
            ItemLoadingBinding.inflate(
                LayoutInflater.from(context)
            ).root
        }
    )
}