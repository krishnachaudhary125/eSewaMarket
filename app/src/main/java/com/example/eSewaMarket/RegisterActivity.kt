package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eSewaMarket.data.models.UserSyncRequest
import com.example.eSewaMarket.databinding.ActivityRegisterBinding
import com.example.eSewaMarket.ui.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(binding.esewaLogo) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        binding.redirectToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.cbTerms.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.uncheckedTermMsg.visibility = View.GONE
            }
        }

        binding.registerBtn.setOnClickListener {

            val firstName = binding.fName.text.toString().trim()
            val middleName = binding.mName.text.toString().trim()
            val lastName = binding.lName.text.toString().trim()
            val phone = binding.phone.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val nameRegex = Regex("^[A-Za-z.]+$")
            val fullName = listOf(firstName, middleName, lastName)
                .filter { it.isNotBlank() }
                .joinToString(" ")

            when {

                firstName.isEmpty() -> {
                    binding.fName.error = "First name is required"
                    binding.fName.requestFocus()
                    return@setOnClickListener
                }

                !firstName.matches(nameRegex) -> {
                    binding.fName.error = "Invalid input"
                    binding.fName.requestFocus()
                    return@setOnClickListener
                }

                middleName.isNotBlank() && !middleName.matches(nameRegex) -> {
                    binding.mName.error = "Invalid input"
                    binding.mName.requestFocus()
                    return@setOnClickListener
                }

                lastName.isEmpty() -> {
                    binding.lName.error = "Last name is required"
                    binding.lName.requestFocus()
                    return@setOnClickListener
                }

                !lastName.matches(nameRegex) -> {
                    binding.lName.error = "Invalid input"
                    binding.lName.requestFocus()
                    return@setOnClickListener
                }

                phone.isEmpty() -> {
                    binding.phone.error = "Phone number is required"
                    binding.phone.requestFocus()
                    return@setOnClickListener
                }

                !phone.matches(Regex("^(?:(\\+977[-.\\s]?)?9[78]\\d{8}|\\+(?!977)[1-9]\\d{6,14})$")) -> {
                    binding.phone.error = "Enter valid phone number"
                    binding.phone.requestFocus()
                    return@setOnClickListener
                }

                phone.length != 10 -> {
                    binding.phone.error = "Enter valid phone number"
                    binding.phone.requestFocus()
                    return@setOnClickListener
                }

                email.isEmpty() -> {
                    binding.email.error = "Email is required"
                    binding.email.requestFocus()
                    return@setOnClickListener
                }

                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.email.error = "Enter valid email"
                    binding.email.requestFocus()
                    return@setOnClickListener
                }

                password.isEmpty() -> {
                    binding.password.error = "Password is required"
                    binding.password.requestFocus()
                    return@setOnClickListener
                }

                password.length < 8 -> {
                    binding.password.error = "Password must be at least 8 characters"
                    binding.password.requestFocus()
                    return@setOnClickListener
                }

                !password.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")) -> {
                    binding.password.error =
                        "Password must contain uppercase, lowercase, number & special character"
                    binding.password.requestFocus()
                    return@setOnClickListener
                }

                !binding.cbTerms.isChecked -> {
                    binding.uncheckedTermMsg.visibility = View.VISIBLE
                    binding.uncheckedTermMsg.error
                    binding.uncheckedTermMsg.text = "❗Please accept the terms and conditions"
                    binding.uncheckedTermMsg.requestFocus()
                    return@setOnClickListener
                }

                else ->
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                binding.loadingOverlay.visibility = View.GONE

                                val intent = Intent(
                                    this,
                                    EmailVerificationActivity::class.java
                                ).apply {
                                    putExtra("fullName", fullName)
                                    putExtra("email", email)
                                    putExtra("phone", phone)
                                    putExtra("fromRegistration", true)
                                }

                                startActivity(intent)
                                finish()

                            } else {

                                binding.loadingOverlay.visibility = View.GONE

                                Log.e(
                                    "FirebaseRegister",
                                    "Registration failed",
                                    task.exception
                                )

                                Toast.makeText(
                                    this,
                                    task.exception?.localizedMessage
                                        ?: "Registration failed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
            }
        }
    }
}