package com.example.eSewaMarket.ui.fragments

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import androidx.viewpager2.widget.ViewPager2
import com.example.eSewaMarket.FeaturedProductActivity
import com.example.eSewaMarket.HotDealProductsActivity
import com.example.eSewaMarket.LoginActivity
import com.example.eSewaMarket.NewAddressActivity
import com.example.eSewaMarket.NotificationActivity
import com.example.eSewaMarket.PopularBrandActivity
import com.example.eSewaMarket.PostProductActivity
import com.example.eSewaMarket.ProductDetailActivity
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.databinding.FragmentHomeBinding
import com.example.eSewaMarket.ui.adapters.BannerPagerAdapter
import com.example.eSewaMarket.ui.adapters.CategoryAdapter
import com.example.eSewaMarket.ui.adapters.HotDealCategoryAdapter
import com.example.eSewaMarket.ui.adapters.ProductAdapter
import com.example.eSewaMarket.ui.adapters.RecommendedProductAdapter
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import com.example.eSewaMarket.ui.viewmodel.HomeViewModel
import com.example.eSewaMarket.ui.viewmodel.RecommendedProductViewModel
import com.example.eSewaMarket.utils.AuthNavigator
import com.example.eSewaMarket.utils.SnackBarUtil
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kotlin.getValue

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()
    private val recommendedProductViewModel: RecommendedProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels {
        ViewModelFactoryProvider.cartFactory(requireContext())
    }

    private val favouriteViewModel: FavouriteViewModel by activityViewModels {
        ViewModelFactoryProvider.favouriteFactory(requireContext())
    }
    private lateinit var bannerAdapter: BannerPagerAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var hotDealCategoryAdapter: HotDealCategoryAdapter
    private lateinit var popularBrandProductAdapter: ProductAdapter
    private lateinit var featuredProductAdapter: ProductAdapter
    private lateinit var hotDealProductAdapter: ProductAdapter
    private lateinit var recommendedAdapter: RecommendedProductAdapter
    private lateinit var userSessionRepository: UserSessionRepository
    private lateinit var authNavigator: AuthNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        userSessionRepository = UserSessionRepository(requireContext())
        authNavigator = AuthNavigator(userSessionRepository)
        ViewCompat.setOnApplyWindowInsetsListener(binding.shopScrollLayer) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        setupBannerViewPager()
        setupCategoryRecyclerView()
        setupHotDealCategoryRecyclerView()
        setupPopularBrandRecyclerView()
        setupFeaturedProductRecyclerView()
        setupHotDealProductRecyclerView()
        setupRecommendedRecyclerView()
        observeData()

        binding.homeAppBar.notification.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.vpBanner.adapter = bannerAdapter

        TabLayoutMediator(binding.tabLayoutIndicator, binding.vpBanner){_,_ -> }.attach()

        var animator: ValueAnimator? = null
        var btnExpanded = true
        binding.shopScrollLayer.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                if (animator == null) {
                    animator = createAnimator()
                }

                if (scrollY > oldScrollY && btnExpanded) {
                    animator.start()
                    btnExpanded = !btnExpanded
                } else if (scrollY < oldScrollY && !btnExpanded) {
                    animator.reverse()
                    btnExpanded = !btnExpanded
                }

                val totalHeight = v.getChildAt(0).measuredHeight - v.measuredHeight
                if (scrollY >= totalHeight - 200) {
                    recommendedProductViewModel.loadMoreRecommended()
                }
            }
        )

        binding.floatingSellButton.setOnClickListener {
            val intent = Intent(requireContext(), PostProductActivity::class.java)
            startActivity(intent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userSessionRepository.isLoggedIn.collect { isLoggedIn ->

                if (isLoggedIn) {
                    userSessionRepository.user.collect { user ->
                        binding.homeAppBar.userName.text = "${user.name},"
                    }
                } else {
                    userSessionRepository.user.collect {
                        binding.homeAppBar.userName.text = "User,"
                    }
                }
            }
        }

        binding.featuredProductBtn.setOnClickListener {
            val intent = Intent(requireContext(), FeaturedProductActivity::class.java)
            startActivity(intent)
        }

        binding.hotDealProductsBtn.setOnClickListener {
            val intent = Intent(requireContext(), HotDealProductsActivity::class.java)
            startActivity(intent)
        }

        binding.popularBrandBtn.setOnClickListener {
            val intent = Intent(requireContext(), PopularBrandActivity::class.java)
            startActivity(intent)
        }

        binding.setAddressBtn.setOnClickListener {
            startActivity(Intent(requireContext(), NewAddressActivity::class.java))
        }
    }

    private fun initAdapters() {
        bannerAdapter = BannerPagerAdapter {
            Toast.makeText(requireContext(), "Banner: ", Toast.LENGTH_SHORT).show()
        }

        categoryAdapter = CategoryAdapter {
            Toast.makeText(requireContext(), "Category: ", Toast.LENGTH_SHORT).show()
        }

        hotDealCategoryAdapter = HotDealCategoryAdapter {
            Toast.makeText(requireContext(), "Category: ", Toast.LENGTH_SHORT).show()
        }

        popularBrandProductAdapter = createProductAdapter()

        featuredProductAdapter = createProductAdapter()

        hotDealProductAdapter = createProductAdapter()

        recommendedAdapter = createRecommendedProductAdapter()
    }

    private fun setupBannerViewPager() {
        binding.vpBanner.apply {
            adapter = bannerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            isUserInputEnabled = true
        }
    }

    private fun setupCategoryRecyclerView() {
        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupHotDealCategoryRecyclerView() {
        binding.hotDealCategoriesRecyclerView.apply {
            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
            adapter = hotDealCategoryAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupPopularBrandRecyclerView() {
        binding.rvPopularBrand.layoutManager = GridLayoutManager(requireContext(), getProductSpanCount())
        binding.rvPopularBrand.adapter = popularBrandProductAdapter
    }

    private fun setupFeaturedProductRecyclerView() {
        binding.rvFeaturedProduct.layoutManager =  GridLayoutManager(requireContext(), getProductSpanCount())
        binding.rvFeaturedProduct.adapter = featuredProductAdapter
    }

    private fun setupHotDealProductRecyclerView() {
        binding.rvHotDealProduct.layoutManager = GridLayoutManager(requireContext(), getProductSpanCount())
        binding.rvHotDealProduct.adapter = hotDealProductAdapter
    }

    private fun setupRecommendedRecyclerView() {
        val spanCount = getProductSpanCount()
        val layoutManager = GridLayoutManager(requireContext(), spanCount)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (recommendedAdapter.isLoadingFooterShown() &&
                    position == recommendedAdapter.itemCount - 1) spanCount else 1
            }
        }

        binding.rvRecommended.layoutManager = layoutManager
        binding.rvRecommended.adapter = recommendedAdapter
        binding.rvRecommended.isNestedScrollingEnabled = false
    }

    private fun observeData() {
        homeViewModel.banners.observe(viewLifecycleOwner) { banners ->
            bannerAdapter.submitList(banners)
        }

        homeViewModel.category.observe(viewLifecycleOwner) { category ->
            categoryAdapter.submitList(category)
        }

        homeViewModel.hotDealCategories.observe(viewLifecycleOwner) { hotDealCategories ->
            hotDealCategoryAdapter.submitList(hotDealCategories)
        }

        homeViewModel.home.observe(viewLifecycleOwner) { home ->
            featuredProductAdapter.submitList(home.featuredProducts.take(getItemsToShow(1)))
            hotDealProductAdapter.submitList(home.hotDeals.take(getItemsToShow(1)))
            popularBrandProductAdapter.submitList(home.popularBrandProducts.take(getItemsToShow(2)))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.homeError.collect { hasError ->
                    val visible = if (hasError) View.VISIBLE else View.GONE
                    binding.featuredTryAgain.tryAgainLayout.visibility = visible
                    binding.hotDealsTryAgain.tryAgainLayout.visibility = visible
                    binding.mostPopularTryAgain.tryAgainLayout.visibility = visible
                    binding.popularBrandTryAgain.tryAgainLayout.visibility = visible
                    binding.recommendedTryAgain.tryAgainLayout.visibility = visible

                    val gone = if (hasError) View.GONE else View.VISIBLE
                    binding.rvFeaturedProduct.visibility = gone
                    binding.rvHotDealProduct.visibility = gone
                    binding.rvPopularBrand.visibility = gone
                    binding.hotDealCategoriesRecyclerView.visibility = gone
                    binding.rvRecommended.visibility = gone
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

    private fun createAnimator(): ValueAnimator {
        val initSize = binding.floatingSell.measuredWidth
        val animator = ValueAnimator.ofInt(initSize, 0)
        animator.duration = 350

        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            val layoutParams = binding.floatingSell.layoutParams
            layoutParams.width = value
            binding.floatingSell.requestLayout()
        }
        return animator
    }

    private fun getProductSpanCount(): Int {
        return when {
            resources.configuration.screenWidthDp >= 840 -> 4
            resources.configuration.screenWidthDp >= 600 -> 3
            else -> 2
        }
    }

    private fun getItemsToShow(rows: Int): Int {
        return rows * getProductSpanCount()
    }

    private fun createProductAdapter(): ProductAdapter{
        return ProductAdapter(
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            onClick = { product ->
                val intent = Intent(requireContext(), ProductDetailActivity::class.java)
                intent.putExtra("product_id", product.id)
                startActivity(intent)
            },
            onAddToCartClick = { product ->
                viewLifecycleOwner.lifecycleScope.launch {
                    if (authNavigator.isLoggedIn()){
                        cartViewModel.addToCart(product)
                    }else{
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
                    if (authNavigator.isLoggedIn()){
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

    private fun createRecommendedProductAdapter(): RecommendedProductAdapter{
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
                    if (authNavigator.isLoggedIn()){
                        cartViewModel.addToCart(product)
                    }else{
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
                    if (authNavigator.isLoggedIn()){
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