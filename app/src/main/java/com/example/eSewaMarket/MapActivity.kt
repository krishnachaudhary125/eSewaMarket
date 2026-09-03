package com.example.eSewaMarket

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.eSewaMarket.ui.compose.MapScreen

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val latitude = intent.getDoubleExtra(
            "latitude", 0.0
        )

        val longitude = intent.getDoubleExtra(
            "longitude", 0.0
        )

        setContent{
            MapScreen(
                onLocationSelected = {},
                latitude = latitude,
                longitude = longitude,
                onConfirmClick = {
                    finish()
                }
            )
        }
    }
}