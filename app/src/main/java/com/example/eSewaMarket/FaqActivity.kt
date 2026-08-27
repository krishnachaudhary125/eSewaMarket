package com.example.eSewaMarket

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eSewaMarket.databinding.ActivityFaqBinding
import com.example.eSewaMarket.ui.adapters.FaqAdapter
import com.example.eSewaMarket.ui.viewmodel.MoreViewModel

class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding
    private val viewModel: MoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarFaq.toolbarBackTitle) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        binding.toolbarFaq.toolbarTitle.text = "FAQs"

        binding.toolbarFaq.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val adapter = FaqAdapter { faq ->

        }

        binding.rvFaq.layoutManager = LinearLayoutManager(this)
        binding.rvFaq.adapter = adapter

        viewModel.faq.observe(this) { faqList ->
            adapter.submitList(faqList)
        }
    }
}