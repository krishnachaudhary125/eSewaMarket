package com.example.eSewaMarket

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.ui.compose.checkoutConfirmation.ConfirmationData
import com.example.eSewaMarket.ui.compose.checkoutConfirmation.ConfirmationScreen
import com.example.eSewaMarket.ui.compose.checkoutConfirmation.ConfirmationViewModel
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.utils.AuthNavigator
import kotlin.getValue

class ConfirmationActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactoryProvider.cartFactory(this)
    }

    private val confirmationViewModel: ConfirmationViewModel by viewModels()

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

            val uiState by confirmationViewModel.uiState
                .collectAsStateWithLifecycle()

            val confirmationData = ConfirmationData(
                checkoutProducts = products,
                shippingAddress = intent.getStringExtra("shippingAddress").orEmpty(),
                paymentOption = intent.getStringExtra("paymentOption").orEmpty(),
                totalAmount = intent.getDoubleExtra("totalPrice", 0.0),
                taxAmount = intent.getDoubleExtra("totalTax", 0.0),
                deliveryCharge = intent.getDoubleExtra("deliveryCharge", 0.0),
                grandTotal = intent.getDoubleExtra("grandTotal", 0.0)
            )

            LaunchedEffect(products) {

                confirmationViewModel.loadConfirmation(
                    confirmationData = confirmationData
                )
            }

            ConfirmationScreen(
                state = uiState,
                onBackClick = {
                    onBackPressedDispatcher.onBackPressed()
                },
                onRetry = {
                    confirmationViewModel.retry()
                }
            )
        }
    }
}