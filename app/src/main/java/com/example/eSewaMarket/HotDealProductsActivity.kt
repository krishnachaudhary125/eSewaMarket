package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.databinding.ActivityHotDealProductsBinding
import com.example.eSewaMarket.ui.compose.SectionProductScreen
import com.example.eSewaMarket.ui.factory.SectionProductViewModelFactory
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import com.example.eSewaMarket.ui.viewmodel.SectionProductViewModel
import com.example.eSewaMarket.utils.AuthNavigator
import com.example.eSewaMarket.utils.SnackBarUtil
import kotlinx.coroutines.launch
import kotlin.getValue

class HotDealProductsActivity : AppCompatActivity(){

    private lateinit var binding: ActivityHotDealProductsBinding
    private val hotDealProductViewModel: SectionProductViewModel by viewModels {
        SectionProductViewModelFactory("hot-deals")
    }
    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactoryProvider.cartFactory(this)
    }
    private val favouriteViewModel: FavouriteViewModel by viewModels {
        ViewModelFactoryProvider.favouriteFactory(this)
    }
    private lateinit var userSessionRepository: UserSessionRepository
    private lateinit var authNavigator: AuthNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityHotDealProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.tbHotDealProducts.root) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.composeView){ view, insets ->
            val bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                bottom
            )
            insets
        }

        binding.tbHotDealProducts.backBtn.setOnClickListener {
            onBackPressedDispatcher
                .onBackPressed()
        }

        binding.tbHotDealProducts.toolbarTitle.text = "Hot Deals of The Day"

        userSessionRepository = UserSessionRepository(this)
        authNavigator = AuthNavigator(userSessionRepository)

        binding.composeView.setContent {

            SectionProductScreen(
                viewModel = hotDealProductViewModel,
                cartViewModel = cartViewModel,
                favouriteViewModel = favouriteViewModel,

                onClick = { product ->
                    val intent = Intent(
                        this@HotDealProductsActivity,
                        ProductDetailActivity::class.java
                    )

                    intent.putExtra("product_id", product.id)
                    startActivity(intent)
                },

                onAddToCartClick = { product ->
                    lifecycleScope.launch {
                        if (authNavigator.isLoggedIn()) {
                            cartViewModel.addToCart(product)
                        } else {
                            SnackBarUtil.show(
                                view = binding.root,
                                context = this@HotDealProductsActivity,
                                text = "Login to continue.",
                                actionText = "GO TO LOGIN"
                            ) {
                                startActivity(
                                    Intent(
                                        this@HotDealProductsActivity,
                                        LoginActivity::class.java
                                    )
                                )
                            }
                        }
                    }
                },

                onRemoveOneFromCart = { product ->
                    lifecycleScope.launch {
                        if (authNavigator.isLoggedIn()) {
                            cartViewModel.removeOneFromCart(product.id)
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
                                context = this@HotDealProductsActivity,
                                text = "Login to continue.",
                                actionText = "GO TO LOGIN"
                            ) {
                                startActivity(
                                    Intent(
                                        this@HotDealProductsActivity,
                                        LoginActivity::class.java
                                    )
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}