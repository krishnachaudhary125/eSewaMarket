package com.example.eSewaMarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.eSewaMarket.databinding.ActivityPostProductBinding
import com.example.eSewaMarket.ui.fragments.PostProductFragment

class PostProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.postProductLayout){ view, insets ->

            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }

        loadFragment(PostProductFragment())

        binding.postProductToolbar.backBtn.setOnClickListener {
            onBackPressedDispatcher
                .onBackPressed()
        }

        binding.postProductToolbar.toolbarTitle.text = "Post Product"
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.postProductFrame, fragment)
            .commit()
    }
}