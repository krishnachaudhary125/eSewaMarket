package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.eSewaMarket.data.api.RetrofitInstance
import com.example.eSewaMarket.data.repository.CartRepository
import com.example.eSewaMarket.data.repository.FavouriteRepository
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.databinding.ActivityLoginBinding
import com.example.eSewaMarket.ui.factory.CartViewModelFactory
import com.example.eSewaMarket.ui.factory.FavouriteViewModelFactory
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import com.example.eSewaMarket.ui.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var cartViewModel: CartViewModel
    private lateinit var favouriteViewModel: FavouriteViewModel

    private lateinit var userSessionRepository: UserSessionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        userSessionRepository = UserSessionRepository(this)

        val database =
            (application as EsewaMarketApplication).database

        val cartRepository = CartRepository(
            cartDao = database.cartDao(),
            productDao = database.productDao(),
            userRepository = userSessionRepository,
            apiService = RetrofitInstance.api
        )

        val favouriteRepository = FavouriteRepository(
            favouriteDao = database.favouriteDao(),
            productDao = database.productDao(),
            userRepository = userSessionRepository,
            apiService = RetrofitInstance.api
        )

        cartViewModel = ViewModelProvider(
                this,
                CartViewModelFactory(cartRepository)
            )[CartViewModel::class.java]

        favouriteViewModel =
            ViewModelProvider(
                this,
                FavouriteViewModelFactory(
                    favouriteRepository
                )
            )[FavouriteViewModel::class.java]

        observeViewModels()

        setupWindowInsets()

        setupClickListeners()
    }

    private fun observeViewModels() {

        userViewModel.loading.observe(this) { isLoading ->

            binding.loadingOverlay.visibility =
                if (isLoading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

            binding.progressBar.visibility =
                if (isLoading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

        userViewModel.user.observe(this) { user ->

            if (user == null) {
                return@observe
            }

            cartViewModel.syncCartWithServer()

            favouriteViewModel
                .syncFavouritesWithServer()

            val intent = Intent(
                this@LoginActivity,
                MainActivity::class.java
            ).apply {

                putExtra(
                    "login_success",
                    true
                )
            }

            startActivity(intent)
            finish()
        }

        userViewModel.error.observe(this) { error ->

            binding.loadingOverlay.visibility =
                View.GONE

            binding.progressBar.visibility =
                View.GONE

            binding.loginBtn.isEnabled = true

            if (!error.isNullOrEmpty()) {

                Toast.makeText(
                    this,
                    error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupWindowInsets() {

        ViewCompat.setOnApplyWindowInsetsListener(
            binding.esewaLogo
        ) { view, insets ->

            val top = insets.getInsets(
                WindowInsetsCompat.Type.statusBars()
            ).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }
    }

    private fun setupClickListeners() {

        binding.redirectToRegister.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }

        binding.loginBtn.setOnClickListener {

            val email =
                binding.loginEmail
                    .text
                    .toString()
                    .trim()

            val password =
                binding.password
                    .text
                    .toString()
                    .trim()

            when {

                email.isEmpty() -> {

                    binding.loginEmail.error =
                        "Email is required"

                    binding.loginEmail.requestFocus()

                    return@setOnClickListener
                }

                password.isEmpty() -> {

                    binding.password.error =
                        "Password is required"

                    binding.password.requestFocus()

                    return@setOnClickListener
                }
            }

            binding.loadingOverlay.visibility =
                View.VISIBLE

            binding.progressBar.visibility =
                View.VISIBLE

            binding.loginBtn.isEnabled = false

            loginUser(
                email,
                password
            )
        }
    }

    private fun loginUser(
        email: String,
        password: String
    ) {

        auth.signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener { task ->

            if (!task.isSuccessful) {

                handleLoginError(
                    task.exception?.localizedMessage
                        ?: "Login failed"
                )

                return@addOnCompleteListener
            }

            val user = auth.currentUser

            if (user == null) {

                handleLoginError(
                    "User with provided credential not found."
                )

                return@addOnCompleteListener
            }

            if (!user.isEmailVerified) {

                binding.loadingOverlay.visibility =
                    View.GONE

                binding.progressBar.visibility =
                    View.GONE

                binding.loginBtn.isEnabled = true

                val intent = Intent(
                    this,
                    EmailVerificationActivity::class.java
                ).apply {

                    putExtra(
                        "fromRegistration",
                        false
                    )
                }

                startActivity(intent)
                finish()

                return@addOnCompleteListener
            }

            getFirebaseToken()
        }
    }

    private fun getFirebaseToken() {

        val user = auth.currentUser

        if (user == null) {

            handleLoginError(
                "User authentication failed"
            )

            return
        }

        user.getIdToken(true)
            .addOnSuccessListener { result ->

                val token = result.token

                if (token != null) {

                    userViewModel
                        .getCurrentUser(token)

                } else {

                    handleLoginError(
                        "Login failed"
                    )
                }
            }
            .addOnFailureListener { exception ->

                handleLoginError(
                    exception.localizedMessage
                        ?: "Login Failed"
                )
            }
    }

    private fun handleLoginError(
        message: String
    ) {

        binding.loadingOverlay.visibility =
            View.GONE

        binding.progressBar.visibility =
            View.GONE

        binding.loginBtn.isEnabled = true

        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}