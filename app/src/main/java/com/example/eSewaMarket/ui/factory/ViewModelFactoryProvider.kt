package com.example.eSewaMarket.ui.factory

import android.content.Context
import com.example.eSewaMarket.EsewaMarketApplication
import com.example.eSewaMarket.data.api.RetrofitInstance
import com.example.eSewaMarket.data.repository.AddressRepository
import com.example.eSewaMarket.data.repository.CartRepository
import com.example.eSewaMarket.data.repository.FavouriteRepository
import com.example.eSewaMarket.data.repository.UserSessionRepository

object ViewModelFactoryProvider {

    fun cartFactory(context: Context) : CartViewModelFactory{
        val app = context.applicationContext as EsewaMarketApplication

        return CartViewModelFactory(
            CartRepository(
                app.database.cartDao(),
                app.database.productDao(),
                UserSessionRepository(app.applicationContext),
                RetrofitInstance.api
            )
        )
    }

    fun favouriteFactory(context: Context) : FavouriteViewModelFactory{
        val app = context.applicationContext as EsewaMarketApplication

        return FavouriteViewModelFactory(
            FavouriteRepository(
                app.database.favouriteDao(),
                app.database.productDao(),
                UserSessionRepository(app.applicationContext),
                RetrofitInstance.api
            )
        )
    }

    fun addressFactory(context: Context) : AddressViewModelFactory{
        val app = context.applicationContext as EsewaMarketApplication

        return AddressViewModelFactory(
            AddressRepository(
                app.database.addressDao(),
                UserSessionRepository(app.applicationContext),
                RetrofitInstance.api
            )
        )
    }
}