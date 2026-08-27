package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eSewaMarket.databinding.ActivityShipptingAddressBinding

class ShippingAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShipptingAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityShipptingAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarShippingAddress.toolbarBackTitle) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        binding.toolbarShippingAddress.toolbarTitle.text = "Shipping Address"
        binding.toolbarShippingAddress.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.addAddressNow.setOnClickListener {
            val intent = Intent(this, NewAddressActivity::class.java)
            startActivity(intent)
        }
    }
}