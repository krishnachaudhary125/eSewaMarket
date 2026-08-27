package com.example.eSewaMarket

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.databinding.ActivityProductDetailBinding
import com.example.eSewaMarket.ui.adapters.OptionAdapter
import com.example.eSewaMarket.ui.adapters.ProductImageAdapter
import com.example.eSewaMarket.ui.adapters.SimilarProductAdapter
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import com.example.eSewaMarket.ui.viewmodel.ProductDetailViewModel
import com.example.eSewaMarket.utils.AuthNavigator
import com.example.eSewaMarket.utils.SnackBarUtil
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()
    private lateinit var imageGalleryAdapter: ProductImageAdapter
    private lateinit var similarProductAdapter: SimilarProductAdapter
    private lateinit var authNavigator: AuthNavigator
    private lateinit var userSessionRepository: UserSessionRepository
    private val optionAdapters = mutableMapOf<String, OptionAdapter>()
    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactoryProvider.cartFactory(this)
    }
    private val favouriteViewModel: FavouriteViewModel by viewModels {
        ViewModelFactoryProvider.favouriteFactory(this)
    }
    private var id = -1L
    private var width = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarProductDetail.toolbarBackAction) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        id = intent.getLongExtra("product_id", -1L)

        binding.toolbarProductDetail.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.toolbarProductDetail.toolbarIcon.setImageResource(R.drawable.ic_cart)
        binding.toolbarProductDetail.toolbarIcon.setBackgroundResource(R.drawable.bg_cart)
        binding.toolbarProductDetail.toolbarIcon.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.esewa_bg_light))

        userSessionRepository = UserSessionRepository(applicationContext)
        authNavigator = AuthNavigator(userSessionRepository)

        val screenWidth = resources.displayMetrics.widthPixels
        val desiredWidth = (screenWidth * 0.45f).toInt()

        val maxWidth = (180 * resources.displayMetrics.density).toInt()
        width = minOf(desiredWidth, maxWidth)

        similarProductAdapter = createProductAdapter()

        val productId = intent.getLongExtra("product_id", -1)
        if(productId == -1L){
            finish()
            return
        }

        setupImageGallery()
        setupOptionRecyclerView("Size", binding.rvSizeOption)
        observeProduct()
        observeCartQuantity()
        observeFavourite()
        setupSimilarProductRecyclerView()

        viewModel.loadProduct(productId)
        viewModel.loadSimilarProducts()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.cartCount().collect { count ->
                    if (count > 0){
                        binding.toolbarProductDetail.numOfProductInCart.text = count.toString()
                        binding.toolbarProductDetail.numOfProductInCart.visibility = View.VISIBLE
                    }else{
                        binding.toolbarProductDetail.numOfProductInCart.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.similarProducts.observe(this) { products ->
            similarProductAdapter.submitList(products.take(5))
            binding.rvSimilarProduct.post {
                binding.rvSimilarProduct.scrollToPosition(0)
            }
        }
    }

    private fun setupImageGallery(){
        imageGalleryAdapter = ProductImageAdapter()
        binding.vpProductImage.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            isUserInputEnabled = true
        }
    }

    private fun setupOptionRecyclerView(
        optionName: String,
        recyclerView: RecyclerView
    ) {
        val adapter = OptionAdapter()
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        optionAdapters[optionName] = adapter
    }

    private fun setupSimilarProductRecyclerView() {

        binding.rvSimilarProduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.rvSimilarProduct.adapter = similarProductAdapter
    }

    private fun observeProduct(){
        viewModel.selectedProduct.observe(this){ product ->
            binding.productName.text = product.title
            binding.productPrice.text = "Rs.${product.price}"
            if(product.stock != 0){
                binding.productStock.text = "In Stock"
                binding.productStock.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.green)
                )
                binding.addToCartBG.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, R.color.green)
                )
                binding.bottomAddToCartBtn.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.white)
                )
                binding.bottomAddToCartBtn.isEnabled = true
            }
            else{
                binding.productStock.text = "Out of Stock"
                binding.productStock.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.red)
                )
                binding.addToCartBG.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, R.color.addToCartSoldOut)
                )
                binding.bottomAddToCartBtn.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.text_dark)
                )
                binding.bottomAddToCartBtn.isEnabled = false
            }

            val smooth = AccelerateDecelerateInterpolator()

            binding.productDescription.text = product.description
            binding.productDescription.setAnimationDuration(750L)
            binding.productDescription.expandInterpolator = smooth
            binding.productDescription.collapseInterpolator = smooth

            binding.viewMoreToggle.setOnClickListener {
                binding.productDescription.toggle()
                if (binding.productDescription.isExpanded){
                    binding.productDescription.collapse()
                    binding.viewMoreToggle.text = "View More"
                }else{
                    binding.productDescription.expand()
                    binding.viewMoreToggle.text = "View Less"
                }
            }

            setRating(product.rating.toFloat())
            imageGalleryAdapter.submitList(product.images)

            binding.vpProductImage.adapter = imageGalleryAdapter

            if (imageGalleryAdapter.itemCount > 1){
                TabLayoutMediator(binding.imageDotIndicator, binding.vpProductImage){_,_ -> }.attach()
            }

            optionVisibility(product,"Size", binding.sizeLabel, binding.rvSizeOption)
            optionVisibility(product, "Color", binding.colorLabel, binding.colorRadioBtn)

            optionAdapters["Size"]?.submitList(product.options["Size"] ?: emptyList())

            binding.productTitle.text = product.title
            binding.bottomProductPrice.text = "Rs.${product.price}"

            if(product.discountPercentage != null){
                val amount = product.price
                val discountAmount =
                    (product.discountPercentage.times(amount) / 100)

                val priceBeforeDiscount = amount + discountAmount

                val struckPrice = SpannableString("Rs. ${String.format("%.2f", priceBeforeDiscount)}")
                struckPrice.setSpan(
                    StrikethroughSpan(),
                    0,
                    struckPrice.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.originalPrice.text = struckPrice
                binding.originalPrice.visibility = View.VISIBLE
            }else{
                binding.originalPrice.visibility = View.GONE
            }

            binding.toolbarProductDetail.toolbarIcon.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("open_fragment", "cart")
                startActivity(intent)
            }

            binding.bottomAddToCartBtn.setOnClickListener {
                lifecycleScope.launch {
                    if(authNavigator.isLoggedIn()){
                        cartViewModel.increaseQuantity(id)
                        SnackBarUtil.show(
                            view = binding.root,
                            context = this@ProductDetailActivity,
                            text = "Added to cart successfully.",
                            anchorView = binding.productDetailBottomNav,
                            actionText = "GO TO CART"
                        ) {
                            val intent = Intent(this@ProductDetailActivity, MainActivity::class.java)
                            intent.putExtra("open_fragment", "cart")
                            startActivity(intent)
                        }
                    }else{
                        SnackBarUtil.show(
                            view = binding.root,
                            context = this@ProductDetailActivity,
                            text = "Login to continue.",
                            anchorView = binding.productDetailBottomNav,
                            actionText = "GO TO LOGIN"
                        ) {
                            val intent = Intent(this@ProductDetailActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }

            binding.plusProductBtn.setOnClickListener {
                cartViewModel.increaseQuantity(id)
            }

            binding.minusProductBtn.setOnClickListener {
                cartViewModel.removeOneFromCart(id)
            }

            binding.favBtn.setOnClickListener {
                lifecycleScope.launch {
                    if (authNavigator.isLoggedIn()) {

                        val wasFavourite = favouriteViewModel.isFavourite(id).first()
                        favouriteViewModel.toggleFavourite(product)

                        SnackBarUtil.show(
                            view = binding.root,
                            context = this@ProductDetailActivity,
                            text = if (wasFavourite)
                                "Removed from favourites."
                            else
                                "Added to favourites.",
                            anchorView = binding.productDetailBottomNav,
                            actionText = if (wasFavourite) null else "GO TO FAVOURITE"
                        ) {
                            startActivity(
                                Intent(this@ProductDetailActivity, MainActivity::class.java)
                                    .putExtra("open_fragment", "favourite")
                            )
                        }

                    } else {
                        SnackBarUtil.show(
                            view = binding.root,
                            context = this@ProductDetailActivity,
                            text = "Login to continue.",
                            anchorView = binding.productDetailBottomNav,
                            actionText = "GO TO LOGIN"
                        ) {
                            startActivity(
                                Intent(this@ProductDetailActivity, LoginActivity::class.java)
                            )
                        }
                    }
                }
            }

            binding.avgRating.text = product.rating.toString()
            binding.totalReviews.text = product.reviewCount.toString()
        }
    }
    fun optionVisibility(
        product: Product,
        optionName: String,
        label: View,
        radioGroup: View
    ) {
        val visibility = if (product.options.containsKey(optionName)) {
            View.VISIBLE
        } else {
            View.GONE
        }

        label.visibility = visibility
        radioGroup.visibility = visibility
    }

    private var quantityJob: Job? = null
    private fun observeCartQuantity() {
        quantityJob?.cancel()

        quantityJob = lifecycleScope.launch {
            cartViewModel.productQuantity(id).collect { qty ->

                binding.tvCartCount.text = qty.toString()

                val btnVisibility = if (qty == 0) View.VISIBLE else View.GONE
                binding.bottomAddToCartBtn.visibility = btnVisibility

                val visible = if (qty > 0) View.VISIBLE else View.GONE
                binding.plusProductBtn.visibility = visible
                binding.tvCartCount.visibility = visible
                binding.minusProductBtn.visibility = visible
            }
        }
    }

    private var favouriteJob: Job? = null
    private fun observeFavourite() {
        favouriteJob?.cancel()

        favouriteJob = lifecycleScope.launch {
            favouriteViewModel.isFavourite(id).collect { isFav ->
                binding.favBtn.setImageResource(
                    if (isFav)
                        R.drawable.ic_fav_filled_white
                    else
                        R.drawable.ic_fav
                )
            }
        }
    }

    private fun setRating(rating: Float){
        val rounded = (rating * 2).roundToInt() / 2f

        val stars = listOf(
            binding.ratingStar.star1,
            binding.ratingStar.star2,
            binding.ratingStar.star3,
            binding.ratingStar.star4,
            binding.ratingStar.star5
        )
        
        stars.forEachIndexed { index, view ->
            val value = rounded -  index

            view.setImageResource(
                when{
                    value >= 1f -> R.drawable.ic_star_filled
                    value >= 0.5f -> R.drawable.ic_star_half
                    else -> R.drawable.ic_star_empty
                }
            )
        }
    }

    private fun createProductAdapter(): SimilarProductAdapter{
        return SimilarProductAdapter(
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            itemWidth = width,
            onClick = { product ->
                val intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra("product_id", product.id)
                startActivity(intent)
            },
            onAddToCartClick = { product ->
                lifecycleScope.launch {
                    if (authNavigator.isLoggedIn()){
                        cartViewModel.addToCart(product)
                    }else{
                        SnackBarUtil.show(
                            view = binding.root,
                            context = this@ProductDetailActivity,
                            text = "Login to continue.",
                            actionText = "GO TO LOGIN"
                        ) {
                            val intent = Intent(this@ProductDetailActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            },
            onRemoveOneFromCartClick = { productId ->
                lifecycleScope.launch {
                    if (authNavigator.isLoggedIn()){
                        cartViewModel.removeOneFromCart(productId)
                    }
                }
            },
            onFavouriteClick = { product ->
                lifecycleScope.launch {
                    if (authNavigator.isLoggedIn()) {
                        favouriteViewModel.toggleFavourite(product)
                    } else {
                        SnackBarUtil.show(
                            view = binding.root,
                            context = this@ProductDetailActivity,
                            text = "Login to continue.",
                            actionText = "GO TO LOGIN"
                        ) {
                            startActivity(Intent(this@ProductDetailActivity, LoginActivity::class.java))
                        }
                    }
                }
            }
        )
    }
}