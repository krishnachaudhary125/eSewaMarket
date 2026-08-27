package com.example.eSewaMarket.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.Banner

class BannerRepository {

    private val _banners = MutableLiveData<List<Banner>>()
    val banners: LiveData<List<Banner>> = _banners

    fun fetchBanners() {

        _banners.value = listOf(
            Banner(
                id = 1,
                imageUrl = "android.resource://com.example.eSewaMarket/${R.drawable.banner1}",
                actionUrl = null
            ),
            Banner(
                id = 2,
                imageUrl = "android.resource://com.example.eSewaMarket/${R.drawable.banner2}",
                actionUrl = null
            ),
            Banner(
                id = 3,
                imageUrl = "android.resource://com.example.eSewaMarket/${R.drawable.banner3}",
                actionUrl = null
            )
        )
    }
}