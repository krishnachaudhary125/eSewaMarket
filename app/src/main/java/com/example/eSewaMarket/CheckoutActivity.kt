package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eSewaMarket.ui.compose.CheckoutScreen
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import kotlin.getValue

class CheckoutActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactoryProvider.cartFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        setContent {
            val products by cartViewModel.cartProducts()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList()
                )

            val productPrice by cartViewModel.totalPrice
                .collectAsStateWithLifecycle(
                    initialValue = 0.0
                )
            val priceProductOnly = productPrice ?: 0.00
            val taxAmount = (priceProductOnly * 13)/100
            val shippingCharge = 70.00

            val totalAmount = priceProductOnly + taxAmount + shippingCharge

            val count by cartViewModel.cartCount()
                .collectAsStateWithLifecycle(
                    initialValue = 0
                )

            CheckoutScreen(
                checkoutProducts = products,
                onBackClick = {
                    onBackPressedDispatcher.onBackPressed()
                },
                totalPrice = totalAmount,
                itemCount = count,
                productPrice = priceProductOnly,
                totalTax = taxAmount,
                shippingCharge = shippingCharge,
                address = "Add Shipping Address",
                onAddMapClick = {
                    val intent = Intent(this, MapActivity::class.java)
                    startActivity(intent)
                },
                onProductClick = {}
            )
        }
    }
}