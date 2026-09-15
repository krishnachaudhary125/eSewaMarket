package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eSewaMarket.data.models.AddressResponse
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.ui.compose.shippingAddress.ShippingAddressScreen
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.compose.shippingAddress.AddressViewModel
import com.example.eSewaMarket.utils.AuthNavigator
import com.example.eSewaMarket.utils.SnackBarUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

            ShippingAddressScreen(
                uiState = uiState,
                onBackClick = {
                    onBackPressedDispatcher
                        .onBackPressed()
                },
                addAddressNow = {
                    val intent = Intent(this, NewAddressActivity::class.java)
                    startActivity(intent)
                },
                onDeleteClick = { address ->
                    deleteAddressDialog(address)
                },
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

    private fun deleteAddressDialog(
        address: AddressResponse
    ) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Do you want to delete this address?")
            .setNegativeButton("No", null)
            .setPositiveButton("Yes") { _, _ ->

                addressViewModel.deleteAddress(
                    id = address.id,
                    onSuccess = {
                        addressViewModel.getAddresses()

                        SnackBarUtil.show(
                            view = findViewById(android.R.id.content),
                            context = this@ShippingAddressActivity,
                            text = "Address has been deleted.",
                            duration = 5000,
                            actionText = "UNDO"
                        ) {

                            addressViewModel.restoreAddress(
                                address = address,
                                onSuccess = {
                                    SnackBarUtil.show(
                                        view = findViewById(android.R.id.content),
                                        context = this@ShippingAddressActivity,
                                        text = "Address restored successfully."
                                    )
                                },
                                onError = { error ->
                                    SnackBarUtil.show(
                                        view = findViewById(android.R.id.content),
                                        context = this@ShippingAddressActivity,
                                        text = error
                                    )
                                }
                            )
                        }
                    }
                )
            }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(
                    ContextCompat.getColor(this, R.color.green)
                )

            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(
                    ContextCompat.getColor(this, R.color.esewa_red)
                )
        }

        dialog.show()
    }
}