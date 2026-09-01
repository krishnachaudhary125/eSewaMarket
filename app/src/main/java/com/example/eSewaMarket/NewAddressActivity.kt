package com.example.eSewaMarket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eSewaMarket.databinding.ActivityNewAddressBinding
import com.example.eSewaMarket.utils.LocationPermissionHandler
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class NewAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewAddressBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationPermissionHandler: LocationPermissionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityNewAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarNewShippingAddress.toolbarBackTitleAction) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        locationPermissionHandler = LocationPermissionHandler(this)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)

        binding.toolbarNewShippingAddress.toolbarTitle.text = "Add your new address"
        binding.toolbarNewShippingAddress.toolbarIcon.setImageResource(R.drawable.ic_close)
        binding.toolbarNewShippingAddress.toolbarIcon.setBackgroundResource(R.drawable.bg_faq_question)
        binding.toolbarNewShippingAddress.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.addLocationIcon.setOnClickListener {
            locationPermissionHandler.runWithLocationPermission {
                fetchCurrentLocationAndOpenMap()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocationAndOpenMap() {

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                if (location == null) {
                    return@addOnSuccessListener
                }

                val latitude = location.latitude
                val longitude = location.longitude

                val intent = Intent(this, MapActivity::class.java).apply {

                    putExtra("latitude", latitude)
                    putExtra("longitude", longitude)
                }

                startActivity(intent)
            }
            .addOnFailureListener { exception ->

                Log.e("LOCATION", "Failed to get location", exception)
            }
    }
}