package com.example.eSewaMarket

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.ui.compose.checkoutConfirmation.ConfirmationScreen
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.utils.AuthNavigator
import kotlin.getValue

class ConfirmationActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactoryProvider.cartFactory(this)
    }

    private lateinit var userSessionRepository: UserSessionRepository
    private lateinit var authNavigator: AuthNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.enableEdgeToEdge(window)

        setContent {

            val products by cartViewModel.cartProducts()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList()
                )

            ConfirmationScreen(
                onBackClick = {
                    onBackPressedDispatcher
                        .onBackPressed()
                },
                checkoutProducts = products,
                shippingAddress = intent.getStringExtra("shippingAddress").toString(),
                paymentOption = intent.getStringExtra("paymentOption").toString(),
                taxAmount = intent.getDoubleExtra("totalTax", 0.0),
                totalAmount = intent.getDoubleExtra("totalPrice", 0.0),
                deliveryCharge = intent.getDoubleExtra("deliveryCharge", 0.0)
            )
        }
    }
}