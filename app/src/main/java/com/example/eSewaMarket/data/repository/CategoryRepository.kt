package com.example.eSewaMarket.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.Category

class CategoryRepository {
    private val _category = MutableLiveData<List<Category>>()
    val category: LiveData<List<Category>> = _category

    fun fetchCategory() {

        _category.value = listOf(
            Category(
                id = 1,
                imageUrl = R.drawable.mobile,
                text = "Mobile",
                actionUrl = null
            ),
            Category(
                id = 2,
                imageUrl = R.drawable.ic_shop_computer,
                text = "Electronic Device",
                actionUrl = null
            ),
            Category(
                id = 3,
                imageUrl = R.drawable.fashion,
                text = "Fashions",
                actionUrl = null
            ),
            Category(
                id = 4,
                imageUrl = R.drawable.ic_shop_grocery,
                text = "Groceries",
                actionUrl = null
            ),
            Category(
                id = 5,
                imageUrl = R.drawable.ic_shop_homestyle,
                text = "Home & Lifestyles",
                actionUrl = null
            ),
            Category(
                id = 6,
                imageUrl = R.drawable.automotive,
                text = "Automotive",
                actionUrl = null
            ),
            Category(
                id = 7,
                imageUrl = R.drawable.baby_care,
                text = "Baby Care",
                actionUrl = null
            )
        )
    }
}