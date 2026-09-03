package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.eSewaMarket.ui.compose.ShippingAddressScreen

class ShippingAddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        setContent {
            ShippingAddressScreen (
                onBackClick = {
                    onBackPressedDispatcher
                        .onBackPressed()
                },
                noOfAddress = 0,
                addAddressNow = {
                    val intent = Intent(this, NewAddressActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}