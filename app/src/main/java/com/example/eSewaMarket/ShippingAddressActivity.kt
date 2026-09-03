package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.ui.compose.ShippingAddressScreen
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.AddressViewModel
import com.example.eSewaMarket.utils.AuthNavigator
import kotlin.collections.emptyList
import kotlin.getValue

class ShippingAddressActivity : AppCompatActivity() {

    private val addressViewModel: AddressViewModel by viewModels {
        ViewModelFactoryProvider.addressFactory(this)
    }
    private lateinit var userSessionRepository: UserSessionRepository
    private lateinit var authNavigator: AuthNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        userSessionRepository = UserSessionRepository(this)
        authNavigator = AuthNavigator(userSessionRepository)
        addressViewModel.getAddresses()

        setContent {

            val addresses by addressViewModel.addresses
                .collectAsStateWithLifecycle(
                    initialValue = emptyList()
                )

            ShippingAddressScreen (
                shippingAddresses = addresses,
                onBackClick = {
                    onBackPressedDispatcher
                        .onBackPressed()
                },
                noOfAddress = addresses.size,
                addAddressNow = {
                    val intent = Intent(this, NewAddressActivity::class.java)
                    startActivity(intent)
                },
                onDeleteClick = {},
                onEditClick = {}
            )
        }
    }
}