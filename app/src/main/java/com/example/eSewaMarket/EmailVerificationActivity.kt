package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import com.example.eSewaMarket.data.models.UserSyncRequest
import com.example.eSewaMarket.ui.compose.EmailVerificationScreen
import com.example.eSewaMarket.ui.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class EmailVerificationActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val userViewModel: UserViewModel by viewModels()
    private var verificationCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.enableEdgeToEdge(window)
        observeViewModel()

        setContent {

            var emailVerified by remember {
                mutableStateOf(false)
            }

            var isSending by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(Unit) {

                while (!emailVerified && !verificationCompleted) {
                    delay(3000)


                    try {

                        val user = auth.currentUser
                        if (user == null) {
                            break
                        }

                        user.reload().await()

                        emailVerified =
                            user.isEmailVerified

                        if (emailVerified) {
                            verificationCompleted = true
                            completeVerification()
                        }

                    } catch (e: Exception) {
                        Log.e("Verification", "Email verification check failed", e)
                    }
                }
            }

            EmailVerificationScreen(

                onBackClick = {
                    auth.signOut()

                    startActivity(
                        Intent(
                            this@EmailVerificationActivity,
                            LoginActivity::class.java
                        ).apply {

                            flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                        Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                    )
                    finish()
                },

                sendEmail = {
                    if (!isSending) {

                        val user = auth.currentUser
                        if (user == null) {

                            Toast.makeText(
                                this@EmailVerificationActivity,
                                "Session expired. Please login again.",
                                Toast.LENGTH_LONG
                            ).show()

                            return@EmailVerificationScreen
                        }

                        isSending = true

                        user.sendEmailVerification()
                            .addOnCompleteListener { task ->

                                isSending = false
                                val message =
                                    if (task.isSuccessful) {
                                        "Verification email sent."
                                    } else {
                                        task.exception?.localizedMessage
                                            ?: "Failed to send verification email"
                                    }

                                Toast.makeText(
                                    this@EmailVerificationActivity,
                                    message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                }
            )
        }
    }

    private fun completeVerification() {

        val user = auth.currentUser
        if (user == null) {

            verificationCompleted = false

            auth.signOut()

            Toast.makeText(
                this,
                "Session expired. Please login again.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        if (!user.isEmailVerified) {
            verificationCompleted = false
            return
        }

        user.getIdToken(true)
            .addOnSuccessListener { result ->

                val token = result.token

                if (token == null) {

                    verificationCompleted = false

                    Toast.makeText(
                        this,
                        "Unable to get Firebase token",
                        Toast.LENGTH_LONG
                    ).show()

                    return@addOnSuccessListener
                }

                val fromRegistration =
                    intent.getBooleanExtra(
                        "fromRegistration",
                        false
                    )

                if (fromRegistration) {
                    syncNewUser(token)
                } else {
                    userViewModel.getCurrentUser(token)
                }
            }
            .addOnFailureListener { exception ->

                verificationCompleted = false

                Toast.makeText(
                    this,
                    exception.localizedMessage
                        ?: "Authentication failed",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun syncNewUser(token: String) {

        val fullName = intent.getStringExtra("fullName")
            ?: ""

        val email = intent.getStringExtra("email")
            ?: auth.currentUser?.email
            ?: ""

        val phone = intent.getStringExtra("phone")
            ?: ""

        if (fullName.isBlank() || email.isBlank()) {

            verificationCompleted = false

            Toast.makeText(
                this,
                "Registration information is missing.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        val request = UserSyncRequest(
            fullName = fullName,
            email = email,
            phone = phone
        )

        userViewModel.syncUser(
            token,
            request
        )
    }

    private fun observeViewModel() {

        userViewModel.user.observe(this) { user ->

            if (user == null) {
                return@observe
            }

            Toast.makeText(
                this,
                "Email verified successfully",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(
                    "open_fragment",
                    "home"
                )

                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }

        userViewModel.error.observe(this) { error ->

            if (!error.isNullOrEmpty()) {
                verificationCompleted = false

                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }
}