package com.example.eSewaMarket.ui.fragments

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.eSewaMarket.EsewaMarketApplication
import com.example.eSewaMarket.FaqActivity
import com.example.eSewaMarket.LoginActivity
import com.example.eSewaMarket.MyReturnActivity
import com.example.eSewaMarket.R
import com.example.eSewaMarket.RegisterActivity
import com.example.eSewaMarket.ShippingAddressActivity
import com.example.eSewaMarket.data.api.RetrofitInstance
import com.example.eSewaMarket.data.repository.AuthRepository
import com.example.eSewaMarket.data.repository.CartRepository
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.databinding.FragmentMoreBinding
import com.example.eSewaMarket.ui.factory.AuthViewModelFactory
import com.example.eSewaMarket.ui.viewmodel.AuthViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class MoreFragment : Fragment() {
    private lateinit var binding: FragmentMoreBinding
    private lateinit var userSessionRepository: UserSessionRepository
    private val authViewModel: AuthViewModel by viewModels {

        val context = requireContext().applicationContext

        val userSessionRepository =
            UserSessionRepository(context)

        AuthViewModelFactory(
            AuthRepository(
                userSessionRepository = userSessionRepository
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        userSessionRepository = UserSessionRepository(requireContext())
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarMore.toolbarBackTitleAction) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        binding.toolbarMore.backBtn.setOnClickListener {
            requireActivity()
                .onBackPressedDispatcher
                .onBackPressed()
        }
        binding.toolbarMore.toolbarTitle.text = "More"
        binding.toolbarMore.toolbarIcon.setImageResource(R.drawable.ic_more_vertical)
        binding.toolbarMore.toolbarIcon.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.esewa_bg_light))

        binding.login.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.register.setOnClickListener {
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.faq.setOnClickListener {
            val intent = Intent(requireContext(), FaqActivity::class.java)
            startActivity(intent)
        }

        binding.myReturn.setOnClickListener {
            val intent = Intent(requireContext(), MyReturnActivity::class.java)
            startActivity(intent)
        }

        binding.shippingAddress.setOnClickListener {
            val intent = Intent(requireContext(), ShippingAddressActivity::class.java)
            startActivity(intent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userSessionRepository.isLoggedIn.collect { isLoggedIn ->

                if (isLoggedIn) {
                    binding.userLogin.visibility = View.GONE
                    binding.profileLayout.visibility = View.VISIBLE
                    binding.underline.visibility = View.VISIBLE
                    binding.myProfileAndMyOrder.visibility = View.VISIBLE
                    binding.logoutBtn.visibility = View.VISIBLE
                } else {
                    binding.userLogin.visibility = View.VISIBLE
                    binding.profileLayout.visibility = View.GONE
                    binding.underline.visibility = View.GONE
                    binding.myProfileAndMyOrder.visibility = View.GONE
                    binding.logoutBtn.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userSessionRepository.user.collect { user ->

                binding.userName.text = user.name
                binding.userPhone.text = user.phone

                if (!user.photoUrl.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(user.photoUrl)
                        .placeholder(R.drawable.profile_image)
                        .error(R.drawable.profile_image)
                        .into(binding.userProfile)
                } else {
                    binding.userProfile.setImageResource(R.drawable.profile_image)
                }
            }
        }

        binding.logoutBtn.setOnClickListener {
            logoutAlertDialog()
        }

        return binding.root
    }

    fun logoutAlertDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Do you want to logout?")
            .setNegativeButton("No", null)
            .setPositiveButton("Yes") { _, _ ->

                binding.loadingOverlay.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
                binding.logoutBtn.isEnabled = false

                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        authViewModel.logout()

                        val loginIntent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(loginIntent)

                        Toast.makeText(requireContext(), "Logout successfully", Toast.LENGTH_SHORT)
                            .show()
                    } catch (e: Exception) {
                        if (isAdded) {
                            Toast.makeText(
                                requireContext(),
                                "Logout failed. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Log.e("LOGOUT", "Logout failed", e)
                    } finally {
                        if (isAdded) {
                            binding.loadingOverlay.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                            binding.logoutBtn.isEnabled = true
                        }
                    }
                }
            }.create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
    }
}