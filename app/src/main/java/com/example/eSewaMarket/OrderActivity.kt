package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eSewaMarket.ui.compose.order.OrderScreen
import com.example.eSewaMarket.ui.compose.order.OrderViewModel
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider

class OrderActivity : AppCompatActivity() {

    private val orderViewModel: OrderViewModel by viewModels {
        ViewModelFactoryProvider.orderFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderViewModel.getAllOrders()

        setContent {

            val uiState by orderViewModel.uiState
                .collectAsStateWithLifecycle()

            OrderScreen(
                uiState = uiState,
                onBackClick = {
                    onBackPressedDispatcher
                        .onBackPressed()
                },
                onRetry = {
                    orderViewModel.retry()
                },
                continueShopping = {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("openFragment", "home")
                    }
                    startActivity(intent)
                }
            )
        }
    }
}