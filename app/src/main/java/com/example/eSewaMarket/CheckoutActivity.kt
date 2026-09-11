package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.ui.compose.checkout.CheckoutData
import com.example.eSewaMarket.ui.compose.checkout.CheckoutScreen
import com.example.eSewaMarket.ui.compose.checkout.CheckoutViewModel
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.AddressViewModel
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.utils.AuthNavigator
import com.example.eSewaMarket.data.models.PaymentOptions
import kotlin.getValue

class CheckoutActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactoryProvider.cartFactory(this)
    }

    private val addressViewModel: AddressViewModel by viewModels {
        ViewModelFactoryProvider.addressFactory(this)
    }

    private val checkoutViewModel: CheckoutViewModel by viewModels()

    private lateinit var userSessionRepository: UserSessionRepository
    private lateinit var authNavigator: AuthNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        userSessionRepository = UserSessionRepository(this)
        authNavigator = AuthNavigator(userSessionRepository)
        addressViewModel.getAddresses()

        setContent {
            val uiState by checkoutViewModel.uiState
                .collectAsStateWithLifecycle()

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

            val shippingAddress = addresses.firstOrNull {
                it.isDefaultAddress
            }

            val shippingAddressText = shippingAddress?.let {
                "${it.addressName}, ${it.district} ${it.postalCode}"
            } ?: "Add Shipping Address"

            val count by cartViewModel.cartCount()
                .collectAsStateWithLifecycle(
                    initialValue = 0
                )

            LaunchedEffect(
                products,
                productPrice,
                count,
                shippingAddressText,
                addressExist
            ) {
                checkoutViewModel.loadCheckout(
                    products = products,
                    productPrice = productPrice ?: 0.0,
                    itemCount = count,
                    address = shippingAddressText,
                    addressExist = addressExist
                )
            }

            CheckoutScreen(
                uiState = uiState,

                onBackClick = {
                    onBackPressedDispatcher.onBackPressed()
                },

                onProductClick = {},

                onSetAddressClick = {
                    val intent = Intent(
                        this,
                        NewAddressActivity::class.java
                    )
                    startActivity(intent)
                },

                chooseAddress = {
                    val intent = Intent(
                        this,
                        ShippingAddressActivity::class.java
                    )
                    startActivity(intent)
                },

                cashOnDelivery = { checkoutData ->

                    shippingAddress?.let { address ->
                        goToConfirmation(
                            PaymentOptions.CASH_ON_DELIVERY,
                            checkoutData,
                            shippingAddressText,
                            address.id
                        )
                    }
                },

                payWithEsewa = { checkoutData ->

                    shippingAddress?.let { address ->
                        goToConfirmation(
                            PaymentOptions.ESEWA,
                            checkoutData,
                            shippingAddressText,
                            address.id
                        )
                    }
                },

                onRetry = {
                    checkoutViewModel.retry()
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        addressViewModel.getAddresses()
    }

    private fun goToConfirmation(
        paymentOption: PaymentOptions,
        checkoutData: CheckoutData,
        shippingAddress: String,
        shippingAddressId: Long
    ){

        val intent = Intent(this, ConfirmationActivity::class.java).apply {
            putExtra("shippingAddress", shippingAddress)
            putExtra("shippingAddressId", shippingAddressId)
            putExtra("paymentOption", paymentOption.name)
            putExtra("deliveryCharge", checkoutData.shippingCharge)
            putExtra("totalTax", checkoutData.totalTax)
            putExtra("totalPrice", checkoutData.productPrice)
            putExtra("grandTotal", checkoutData.totalAmount)
        }
        startActivity(intent)
    }
}