package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eSewaMarket.databinding.ActivityReturnBinding

class MyReturnActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReturnBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityReturnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarMyReturn.toolbarBackTitle) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        binding.toolbarMyReturn.toolbarTitle.text = "My returns"
        binding.toolbarMyReturn.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.continueShopping.setOnClickListener {
            val intent =  Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra("open_fragment", "home")
            }
            startActivity(intent)
        }
    }
}