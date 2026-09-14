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
import com.example.eSewaMarket.ui.compose.shippingAddress.ShippingAddressScreen
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.compose.shippingAddress.AddressViewModel
import com.example.eSewaMarket.utils.AuthNavigator
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
            val uiState by addressViewModel.uiState
                .collectAsStateWithLifecycle()

            ShippingAddressScreen (
                uiState = uiState,
                onBackClick = {
                    onBackPressedDispatcher
                        .onBackPressed()
                },
                addAddressNow = {
                    val intent = Intent(this, NewAddressActivity::class.java)
                    startActivity(intent)
                },
                onDeleteClick = {},
                onEditClick = { addressId ->
                    val intent = Intent(this, NewAddressActivity::class.java).apply {
                        putExtra("addressId", addressId)
                    }
                    startActivity(intent)
                },
                onAddAddressClick = {
                    val intent = Intent(this, NewAddressActivity::class.java)
                    startActivity(intent)
                },
                onRetry = {
                    addressViewModel.retry()
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()

        addressViewModel.getAddresses()
    }
}