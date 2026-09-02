package com.example.eSewaMarket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eSewaMarket.data.models.AddressRequest
import com.example.eSewaMarket.databinding.ActivityNewAddressBinding
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.AddressViewModel
import com.example.eSewaMarket.utils.LocationPermissionHandler
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth

class NewAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewAddressBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationPermissionHandler: LocationPermissionHandler
    private val addressViewModel: AddressViewModel by viewModels {
        ViewModelFactoryProvider.addressFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityNewAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarNewShippingAddress.toolbarBackTitleAction) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        locationPermissionHandler = LocationPermissionHandler(this)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)

        binding.toolbarNewShippingAddress.toolbarTitle.text = "Add your new address"
        binding.toolbarNewShippingAddress.toolbarIcon.setImageResource(R.drawable.ic_close)
        binding.toolbarNewShippingAddress.toolbarIcon.setBackgroundResource(R.drawable.bg_faq_question)
        binding.toolbarNewShippingAddress.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.toolbarNewShippingAddress.toolbarIcon.setOnClickListener {

            binding.etFName.text?.clear()
            binding.etPhone.text?.clear()
            binding.etAddress.text?.clear()
            binding.addrLabelGroup.clearCheck()
            binding.switchShippingAddress.isChecked = false
            binding.switchBillingAddress.isChecked = false
        }

        binding.chooseOnMap.setOnClickListener {
            locationPermissionHandler.runWithLocationPermission {
                fetchCurrentLocationAndOpenMap()
            }
        }

        binding.saveBtn.setOnClickListener {

            val fullName = binding.etFName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()

            val selectedRadioButton =
                binding.addrLabelGroup.findViewById<RadioButton>(
                    binding.addrLabelGroup.checkedRadioButtonId
                )
            val label = selectedRadioButton?.text?.toString()

            val isDefaultAddress = binding.switchShippingAddress.isChecked
            val isBillingAddress = binding.switchBillingAddress.isChecked

            val nameRegex = Regex("^[A-Za-z]+(?: [A-Za-z]+){0,3}$")
            val phoneRegex = Regex("^(?:(\\+977[-.\\s]?)?9[78]\\d{8}|\\+(?!977)[1-9]\\d{6,14})$")
            val addressRegex = Regex("^[\\p{L}\\p{N}\\s.,/#'()-]{1,200}$")

            when {

                fullName.isEmpty() -> {
                    binding.etFName.error = "First name is required"
                    binding.etFName.requestFocus()
                    return@setOnClickListener
                }

                !fullName.matches(nameRegex) -> {
                    binding.etFName.error = "Invalid input"
                    binding.etFName.requestFocus()
                    return@setOnClickListener
                }

                phone.isEmpty() -> {
                    binding.etPhone.error = "Phone number is required"
                    binding.etPhone.requestFocus()
                    return@setOnClickListener
                }

                !phone.matches(phoneRegex) -> {
                    binding.etPhone.error = "Enter valid phone number"
                    binding.etPhone.requestFocus()
                    return@setOnClickListener
                }

                phone.length != 10 -> {
                    binding.etPhone.error = "Enter valid phone number"
                    binding.etPhone.requestFocus()
                    return@setOnClickListener
                }

                address.isEmpty() -> {
                    binding.etAddress.error = "Address is required"
                    binding.etAddress.requestFocus()
                    return@setOnClickListener
                }

                !address.matches(addressRegex) -> {
                    binding.etAddress.error = "Enter valid address"
                    binding.etAddress.requestFocus()
                    return@setOnClickListener
                }

                else -> {

                    val request = AddressRequest(
                        fullName = fullName,
                        phone = phone,
                        addressName = address,
                        formattedAddress = null,
                        isDefaultAddress = isDefaultAddress,
                        isBillingAddress = isBillingAddress,
                        label = label
                    )

                    addressViewModel.createAddress(
                        request = request,
                        onSuccess = {
                            Toast.makeText(this, "Address saved successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocationAndOpenMap() {

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                if (location == null) {
                    return@addOnSuccessListener
                }

                val latitude = location.latitude
                val longitude = location.longitude

                val intent = Intent(this, MapActivity::class.java).apply {

                    putExtra("latitude", latitude)
                    putExtra("longitude", longitude)
                }

                startActivity(intent)
            }
            .addOnFailureListener { exception ->

                Log.e("LOCATION", "Failed to get location", exception)
            }
    }
}