package com.example.eSewaMarket

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eSewaMarket.ui.compose.order.OrderScreen
import com.example.eSewaMarket.ui.compose.order.OrderViewModel

class OrderActivity : AppCompatActivity() {

    private val orderViewModel: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val uiState by orderViewModel.uiState
                .collectAsStateWithLifecycle()

            OrderScreen(
                uiState = uiState,
                onBackClick = {
                    onBackPressedDispatcher
                        .onBackPressed()
                },
                onRetry = {}
            )
        }
    }
}