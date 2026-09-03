package com.example.eSewaMarket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.eSewaMarket.data.models.AddressRequest
import com.example.eSewaMarket.databinding.ActivityNewAddressBinding
import com.example.eSewaMarket.ui.adapters.SpinnerAdapter
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.AddressViewModel
import com.example.eSewaMarket.ui.viewmodel.LocationViewModel
import com.example.eSewaMarket.utils.LocationPermissionHandler
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.collections.mutableListOf

class NewAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewAddressBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationPermissionHandler: LocationPermissionHandler
    private val addressViewModel: AddressViewModel by viewModels {
        ViewModelFactoryProvider.addressFactory(this)
    }
    private val locationViewModel: LocationViewModel by viewModels {
        ViewModelFactoryProvider.locationFactory(this)
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

        setupLocationDropdowns()
        setupDistrictDropdown()

        locationViewModel.loadProvinces()

        binding.toolbarNewShippingAddress.toolbarTitle.text = "Add your new address"
        binding.toolbarNewShippingAddress.toolbarIcon.setImageResource(R.drawable.ic_close)
        binding.toolbarNewShippingAddress.toolbarIcon.setBackgroundResource(R.drawable.bg_faq_question)
        binding.toolbarNewShippingAddress.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.toolbarNewShippingAddress.toolbarIcon.setOnClickListener {

            binding.etFName.text?.clear()
            binding.etPhone.text?.clear()
            binding.province.setSelection(0)
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
            val province = binding.province.selectedItem.toString().trim()
            val district = binding.district.selectedItem.toString().trim()
            val city = binding.city.text.toString().trim()
            val postalCode = binding.postalCode.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()

            val selectedRadioButton =
                binding.addrLabelGroup.findViewById<RadioButton>(
                    binding.addrLabelGroup.checkedRadioButtonId
                )
            val label = selectedRadioButton?.text?.toString()

            val isDefaultAddress = binding.switchShippingAddress.isChecked
            val isBillingAddress = binding.switchBillingAddress.isChecked

            val nameRegex = Regex("^[A-Za-z]+(?: [A-Za-z]+){0,3}$")
            val phoneRegex = Regex("""^(?:9[78]\d{8}|\+977[-.\s]?9[78]\d{8}|\+(?!977)[1-9]\d{6,14})$""")
            val addressRegex = Regex("^[\\p{L}\\p{N}\\s.,/#'()-]{1,200}$")
            val postalCodeRegex = Regex("""^\d{5}$""")

            when {

                fullName.isEmpty() -> {
                    binding.etFName.error = "Full name is required"
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

                binding.province.selectedItemPosition == 0 -> {
                    binding.etProvinceError.error = "Province is required"
                    binding.etProvinceError.visibility = View.VISIBLE
                    binding.etProvinceError.requestFocus()
                    return@setOnClickListener
                }

                binding.district.selectedItemPosition == 0 -> {
                    binding.etDistrictError.error = "District is required"
                    binding.etDistrictError.visibility = View.VISIBLE
                    binding.etDistrictError.requestFocus()
                    return@setOnClickListener
                }

                city.isEmpty() -> {
                    binding.city.error = "City is required"
                    binding.city.requestFocus()
                    return@setOnClickListener
                }

                !city.matches(addressRegex) -> {
                    binding.city.error = "Enter valid city"
                    binding.city.requestFocus()
                    return@setOnClickListener
                }

                postalCode.isEmpty() -> {
                    binding.postalCode.error = "Postal Code is required"
                    binding.postalCode.requestFocus()
                    return@setOnClickListener
                }

                !postalCode.matches(postalCodeRegex) -> {
                    binding.postalCode.error = "Enter a valid postal code"
                    binding.postalCode.requestFocus()
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
                        province = province,
                        district = district,
                        city = city,
                        postalCode = postalCode,
                        addressName = address,
                        isDefaultAddress = isDefaultAddress,
                        isBillingAddress = isBillingAddress,
                        label = label
                    )

                    addressViewModel.createAddress(
                        request = request,
                        onSuccess = {
                            Toast.makeText(this, "Address saved successfully", Toast.LENGTH_SHORT)
                                .show()
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

    private fun setupLocationDropdowns() {

        val provinceNames = mutableListOf("")

        val provinceAdapter = SpinnerAdapter(
            this,
            provinceNames,
            "Choose Province"
        )

        binding.province.adapter = provinceAdapter

        locationViewModel.provinces
            .onEach { provinces ->

                provinceNames.clear()
                provinceNames.add("")
                provinceNames.addAll(
                    provinces.map { it.name }
                )

                provinceAdapter.notifyDataSetChanged()
            }
            .launchIn(lifecycleScope)

        binding.province.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {

                    if (position == 0) {
                        binding.district.isEnabled = false
                        binding.district.setSelection(0)
                        return
                    }

                    val selectedProvince =
                        locationViewModel.provinces.value[position - 1]

                    binding.district.setSelection(0)
                    binding.district.isEnabled = true

                    locationViewModel.loadDistricts(selectedProvince.id)
                }

                override fun onNothingSelected(
                    parent: android.widget.AdapterView<*>?
                ) {
                }
            }
    }

    private fun setupDistrictDropdown() {

        val districtNames = mutableListOf("Choose District")

        val districtAdapter = SpinnerAdapter(
            this,
            districtNames,
            "Choose District"
        )

        binding.district.adapter = districtAdapter
        binding.district.isEnabled = false

        locationViewModel.districts
            .onEach { districts ->

                districtNames.clear()
                districtNames.add("")
                districtNames.addAll(
                    districts.map { it.name }
                )

                districtAdapter.notifyDataSetChanged()

            }
            .launchIn(lifecycleScope)
    }
}