package com.example.eSewaMarket

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eSewaMarket.ui.compose.orderDetail.OrderDetailScreen
import com.example.eSewaMarket.ui.compose.orderDetail.OrderDetailViewModel
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider

class OrderProductDetailActivity: AppCompatActivity() {

    private val orderDetailViewModel: OrderDetailViewModel by viewModels {
        ViewModelFactoryProvider.orderDetailFactory(this)
    }

    private var orderId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderId = intent
            .getLongExtra("orderId", -1L)
            .takeIf { it != -1L }

        setContent {

            val uiState by orderDetailViewModel.uiState
                .collectAsStateWithLifecycle()

            LaunchedEffect(orderId) {
                orderId?.let {
                    orderDetailViewModel.loadOrderDetail(it)
                }
            }

            OrderDetailScreen(
                uiState = uiState,

                onBackClick = {
                    onBackPressedDispatcher
                        .onBackPressed()
                },
                onRetry = {
                    orderDetailViewModel.retry()
                }
            )
        }
    }
}