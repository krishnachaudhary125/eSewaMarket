package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.ui.compose.CheckoutScreen
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.AddressViewModel
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.utils.AuthNavigator
import kotlin.getValue

class CheckoutActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactoryProvider.cartFactory(this)
    }

    private val addressViewModel: AddressViewModel by viewModels {
        ViewModelFactoryProvider.addressFactory(this)
    }

    private lateinit var userSessionRepository: UserSessionRepository
    private lateinit var authNavigator: AuthNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        userSessionRepository = UserSessionRepository(this)
        authNavigator = AuthNavigator(userSessionRepository)
        addressViewModel.getAddresses()

        setContent {
            val products by cartViewModel.cartProducts()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList()
                )

            val productPrice by cartViewModel.totalPrice
                .collectAsStateWithLifecycle(
                    initialValue = 0.0
                )

            val addressExist by addressViewModel.hasAddresses()
                .collectAsStateWithLifecycle(
                    initialValue = false
                )

            val addresses by addressViewModel.addresses
                .collectAsStateWithLifecycle(
                    initialValue = emptyList()
                )

            val shippingAddress = addresses.firstOrNull { it.isDefaultAddress }

            val shippingAddressText = shippingAddress?.let {
                "${it.addressName}, ${it.city}, ${it.district} ${it.postalCode}"
            } ?: "Add Shipping Address"

            val priceProductOnly = productPrice ?: 0.00
            val taxAmount = (priceProductOnly * 13) / 100
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
                address = shippingAddressText,
                onProductClick = {},
                onSetAddressClick = {
                    val intent = Intent(this, NewAddressActivity::class.java)
                    startActivity(intent)
                },
                addressExist = addressExist,
                chooseAddress = {
                    val intent = Intent(this, ShippingAddressActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}