package com.example.eSewaMarket.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.eSewaMarket.data.local.dao.CartDao
import com.example.eSewaMarket.data.local.dao.FavouriteDao
import com.example.eSewaMarket.data.local.dao.ProductDao
import com.example.eSewaMarket.data.local.entity.CartEntity
import com.example.eSewaMarket.data.local.entity.FavouriteEntity
import com.example.eSewaMarket.data.local.entity.ProductEntity

@Database(
    entities = [
        CartEntity::class,
        FavouriteEntity::class,
        ProductEntity::class
    ],
    version = 3
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
    abstract fun favouriteDao(): FavouriteDao

    abstract fun productDao(): ProductDao
}