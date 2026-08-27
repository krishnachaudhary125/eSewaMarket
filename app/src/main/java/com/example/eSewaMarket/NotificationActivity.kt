package com.example.eSewaMarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eSewaMarket.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notificationBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}