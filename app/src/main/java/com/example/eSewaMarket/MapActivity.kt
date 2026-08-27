package com.example.eSewaMarket

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.eSewaMarket.ui.compose.MapScreen

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            MapScreen(
                onLocationSelected = {},
                onSearchClick = {}
            )
        }
    }
}