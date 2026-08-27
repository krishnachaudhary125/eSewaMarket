package com.example.eSewaMarket.ui.fragments

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eSewaMarket.CheckoutActivity
import com.example.eSewaMarket.LoginActivity
import com.example.eSewaMarket.MainActivity
import com.example.eSewaMarket.ProductDetailActivity
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.databinding.FragmentCartBinding
import com.example.eSewaMarket.ui.adapters.CartProductAdapter
import com.example.eSewaMarket.ui.adapters.RecommendedProductAdapter
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import com.example.eSewaMarket.ui.viewmodel.RecommendedProductViewModel
import com.example.eSewaMarket.utils.AuthNavigator
import com.example.eSewaMarket.utils.SnackBarUtil
import kotlinx.coroutines.launch
import kotlin.getValue

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartProductAdapter: CartProductAdapter
    private val recommendedProductViewModel: RecommendedProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels {
        ViewModelFactoryProvider.cartFactory(requireContext())
    }
    private val favouriteViewModel: FavouriteViewModel by activityViewModels {
        ViewModelFactoryProvider.favouriteFactory(requireContext())
    }
    private lateinit var recommendedAdapter: RecommendedProductAdapter
    private lateinit var userSessionRepository: UserSessionRepository
    private lateinit var authNavigator: AuthNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        userSessionRepository = UserSessionRepository(requireContext())
        authNavigator = AuthNavigator(userSessionRepository)
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarCart.toolBarCartFragment) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }

        binding.toolbarCart.backBtn.setOnClickListener {
            requireActivity()
                .onBackPressedDispatcher
                .onBackPressed()
        }
        binding.toolbarCart.toolbarTitle.text = "My Cart"
        binding.toolbarCart.toolbarIcon.setImageResource(R.drawable.ic_cart)
        binding.toolbarCart.toolbarIcon.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.esewa_bg_light))

        binding.continueShoppingBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                putExtra("open_fragment", "home")
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        }

        binding.goToLoginBtn.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.checkoutBtn.setOnClickListener {
            val intent = Intent(requireContext(), CheckoutActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        setupCartRecyclerView()
        setupRecommendedRecyclerView()
        observeData()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.cartCount().collect { count ->

                    if (count > 0) {
                        binding.emptyCartLayout.visibility = View.GONE
                        binding.rvCartProduct.visibility = View.VISIBLE
                        binding.toolbarCart.numOfProductInCart.text = count.toString()
                        binding.toolbarCart.numOfProductInCart.visibility = View.VISIBLE
                    } else {
                        binding.emptyCartLayout.visibility = View.VISIBLE
                        if (authNavigator.isLoggedIn()) {
                            binding.goToLoginBtn.visibility = View.GONE
                            binding.continueShoppingBtn.visibility = View.VISIBLE
                            binding.emptyCartMsg.text = "No items added to the cart yet"
                        } else {
                            binding.goToLoginBtn.visibility = View.VISIBLE
                            binding.continueShoppingBtn.visibility = View.GONE
                            binding.emptyCartMsg.text = "Login to add items"
                        }
                        binding.rvCartProduct.visibility = View.GONE
                        binding.toolbarCart.numOfProductInCart.visibility = View.GONE
                    }
                }
            }
        }

        binding.cartScrollLayer.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                val totalHeight = v.getChildAt(0).measuredHeight - v.measuredHeight
                if (scrollY >= totalHeight - 200) {
                    recommendedProductViewModel.loadMoreRecommended()
                }
            }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.totalPrice.collect { total ->
                    if (total == null) {
                        binding.checkoutLayout.visibility = View.GONE
                    } else {
                        binding.checkoutLayout.visibility = View.VISIBLE
                        binding.tvTotalPrice.text = "Rs. %.2f".format(total)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.totalItem.collect { total ->
                    binding.itemCount.text = "( ${total} )"
                }
            }
        }
    }

    private fun initAdapters() {
        cartProductAdapter = CartProductAdapter(
            onClick = { product ->
                val intent = Intent(
                    requireContext(),
                    ProductDetailActivity::class.java
                )
                intent.putExtra("product_id", product.productId)
                startActivity(intent)
            },

            onAddToCartClick = { productId ->
                cartViewModel.increaseQuantity(productId)
            },

            onRemoveOneFromCartClick = { productId ->
                cartViewModel.removeOneFromCart(productId)
            }
        )

        recommendedAdapter = createRecommendedProductAdapter()
    }

    private fun setupCartRecyclerView() {
        binding.rvCartProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCartProduct.adapter = cartProductAdapter
        binding.rvCartProduct.isNestedScrollingEnabled = false

        binding.rvCartProduct.itemAnimator = null
    }

    private fun setupRecommendedRecyclerView() {
        val spanCount = getProductSpanCount()
        val layoutManager = GridLayoutManager(requireContext(), spanCount)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (recommendedAdapter.isLoadingFooterShown() &&
                    position == recommendedAdapter.itemCount - 1
                ) spanCount else 1
            }
        }

        binding.rvRecommendedCart.layoutManager = layoutManager
        binding.rvRecommendedCart.adapter = recommendedAdapter
        binding.rvRecommendedCart.isNestedScrollingEnabled = false
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                cartViewModel.cartProducts().collect { products ->
                    cartProductAdapter.submitList(products)
                }
            }
        }

        recommendedProductViewModel.recommendedProducts.observe(viewLifecycleOwner) { recommended ->
            recommendedAdapter.submitFullList(recommended)
        }

        recommendedProductViewModel.recommendedLoading.observe(viewLifecycleOwner) { isLoading ->
            recommendedAdapter.setLoading(isLoading)
        }
    }

    private fun getProductSpanCount(): Int {
        return when {
            resources.configuration.screenWidthDp >= 840 -> 4
            resources.configuration.screenWidthDp >= 600 -> 3
            else -> 2
        }
    }

    private fun createRecommendedProductAdapter(): RecommendedProductAdapter {
        return RecommendedProductAdapter(
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            onClick = { product ->
                val intent = Intent(requireContext(), ProductDetailActivity::class.java)
                intent.putExtra("product_id", product.id)
                startActivity(intent)
            },
            onAddToCartClick = { product ->
                viewLifecycleOwner.lifecycleScope.launch {
                    if (authNavigator.isLoggedIn()) {
                        cartViewModel.addToCart(product)
                    } else {
                        val coordinator = requireActivity().findViewById<View>(R.id.main)
                        val bottomNav = requireActivity().findViewById<View>(R.id.bottomNav)

                        SnackBarUtil.show(
                            view = coordinator,
                            context = requireContext(),
                            text = "Login to continue.",
                            anchorView = bottomNav,
                            actionText = "GO TO LOGIN"
                        ) {
                            val intent = Intent(requireContext(), LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            },
            onRemoveOneFromCartClick = { productId ->
                viewLifecycleOwner.lifecycleScope.launch {
                    if (authNavigator.isLoggedIn()) {
                        cartViewModel.removeOneFromCart(productId)
                    }
                }
            },
            onFavouriteClick = { product ->
                viewLifecycleOwner.lifecycleScope.launch {
                    if (authNavigator.isLoggedIn()) {
                        favouriteViewModel.toggleFavourite(product)
                    } else {
                        val coordinator = requireActivity().findViewById<View>(R.id.main)
                        val bottomNav = requireActivity().findViewById<View>(R.id.bottomNav)

                        SnackBarUtil.show(
                            view = coordinator,
                            context = requireContext(),
                            text = "Login to continue.",
                            anchorView = bottomNav,
                            actionText = "GO TO LOGIN"
                        ) {
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                        }
                    }
                }
            }
        )
    }
}