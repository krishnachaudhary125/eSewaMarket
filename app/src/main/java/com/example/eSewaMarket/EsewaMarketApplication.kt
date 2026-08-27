package com.example.eSewaMarket

import android.app.Application
import androidx.room3.Room
import com.example.eSewaMarket.data.local.AppDatabase
import com.google.android.libraries.places.api.Places

class EsewaMarketApplication : Application() {

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "esewa_market_db"
        )
            .fallbackToDestructiveMigration(true)
            .build()


        Places.initializeWithNewPlacesApiEnabled(
            applicationContext,
            getString(R.string.maps_api_key)
        )
    }
}