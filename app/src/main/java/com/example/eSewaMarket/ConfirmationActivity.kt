package com.example.eSewaMarket

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.eSewaMarket.ui.compose.checkoutConfirmation.ConfirmationScreen

class ConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.enableEdgeToEdge(window)

        setContent {
            ConfirmationScreen(
                onBackClick = {
                    onBackPressedDispatcher
                        .onBackPressed()
                },
                singleProductQuantity = 1
            )
        }
    }
}