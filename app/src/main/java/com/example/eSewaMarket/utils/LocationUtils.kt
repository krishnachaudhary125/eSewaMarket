package com.example.eSewaMarket.utils

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun searchLocation(
    context: Context,
    query: String
): LatLng? {

    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context)

            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(
                query,
                1
            )

            addresses?.firstOrNull()?.let {
                LatLng(
                    it.latitude,
                    it.longitude
                )
            }

        } catch (e: Exception) {
            Log.e(
                "LOCATION",
                "Unable to find location",
                e
            )
            null
        }
    }
}