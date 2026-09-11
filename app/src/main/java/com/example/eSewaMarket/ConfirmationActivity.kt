package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eSewaMarket.data.models.PaymentOptions
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.ui.compose.checkoutConfirmation.ConfirmationData
import com.example.eSewaMarket.ui.compose.checkoutConfirmation.ConfirmationNavigationEvent
import com.example.eSewaMarket.ui.compose.checkoutConfirmation.ConfirmationScreen
import com.example.eSewaMarket.ui.compose.checkoutConfirmation.ConfirmationViewModel
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.payment.EsewaPayment
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.utils.AuthNavigator
import com.f1soft.esewapaymentsdk.ui.screens.EsewaPaymentActivity
import kotlin.getValue

class ConfirmationActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactoryProvider.cartFactory(this)
    }

    private val confirmationViewModel: ConfirmationViewModel by viewModels {
        ViewModelFactoryProvider.confirmationFactory(this)
    }

    private lateinit var userSessionRepository: UserSessionRepository
    private lateinit var authNavigator: AuthNavigator

    private lateinit var esewaLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.enableEdgeToEdge(window)

        esewaLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            confirmationViewModel.onEsewaPaymentResult(
                success = result.resultCode == RESULT_OK
            )
        }

        setContent {

            val products by cartViewModel.cartProducts()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList()
                )

            val uiState by confirmationViewModel.uiState
                .collectAsStateWithLifecycle()

            val paymentOption = PaymentOptions.valueOf(
                intent.getStringExtra("paymentOption")
                    ?: PaymentOptions.CASH_ON_DELIVERY.name
            )

            val placedOrder by confirmationViewModel.placedOrder
                .collectAsStateWithLifecycle()

            val isPlacingOrder by confirmationViewModel.isPlacingOrder
                .collectAsStateWithLifecycle()

            val confirmationData = ConfirmationData(
                checkoutProducts = products,
                shippingAddress = intent.getStringExtra("shippingAddress").orEmpty(),
                addressId = intent.getLongExtra("shippingAddressId", -1L),
                paymentOption = paymentOption,
                totalAmount = intent.getDoubleExtra("totalPrice", 0.0),
                taxAmount = intent.getDoubleExtra("totalTax", 0.0),
                deliveryCharge = intent.getDoubleExtra("deliveryCharge", 0.0),
                grandTotal = intent.getDoubleExtra("grandTotal", 0.0)
            )

            LaunchedEffect(Unit) {
                confirmationViewModel.navigationEvent.collect { event ->
                    when (event) {
                        is ConfirmationNavigationEvent.StartEsewaPayment -> {
                            val intent = Intent(this@ConfirmationActivity, EsewaPayment::class.java).apply {
                                putExtra("orderNumber", event.order.orderNumber)
                                putExtra("totalAmount", event.order.totalAmount)
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            esewaLauncher.launch(intent)
                        }
                    }
                }
            }


            LaunchedEffect(products) {

                confirmationViewModel.loadConfirmation(
                    confirmationData = confirmationData
                )
            }

            ConfirmationScreen(
                state = uiState,
                placedOrder = placedOrder,
                isPlacingOrder = isPlacingOrder,
                onBackClick = {
                    onBackPressedDispatcher.onBackPressed()
                },
                onRetry = {
                    confirmationViewModel.retry()
                },
                onConfirmOrder = {
                    confirmationViewModel.createOrder()
                },
                onPayWithEsewa = {
                    confirmationViewModel.createOrder()
                },
                onViewOrderClick = { order ->
                    val intent = Intent(this, OrderActivity::class.java).apply {
                        putExtra("orderNumber", order.orderNumber)
                        putExtra("totalAmount", order.totalAmount)
                    }
                    startActivity(intent)
                    finish()
                },
                onGoToHomeClick = {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("openFragment", "home")
                    }
                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}