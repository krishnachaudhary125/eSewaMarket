package com.example.eSewaMarket.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.eSewaMarket.LoginActivity
import com.example.eSewaMarket.MainActivity
import com.example.eSewaMarket.ProductDetailActivity
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.FavouriteResponse
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.ui.compose.FavouriteFragmentScreen
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import com.example.eSewaMarket.utils.AuthNavigator
import com.example.eSewaMarket.utils.SnackBarUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment() {
    private val favouriteViewModel: FavouriteViewModel by viewModels {
        ViewModelFactoryProvider.favouriteFactory(requireContext())
    }
    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactoryProvider.cartFactory(requireContext())
    }
    private lateinit var userSessionRepository: UserSessionRepository
    private lateinit var authNavigator: AuthNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            userSessionRepository = UserSessionRepository(requireContext())
            authNavigator = AuthNavigator(userSessionRepository)
            setContent {

                var isLoggedIn by remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(Unit) {
                    isLoggedIn = authNavigator.isLoggedIn()
                }

                var selectedIds by remember {
                    mutableStateOf<Set<Long>>(emptySet())
                }

                val products by favouriteViewModel.favouriteProducts()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList()
                    )

                val allSelected =
                    products.isNotEmpty() && selectedIds.size == products.size

                val selectedCount = selectedIds.size

                val cartCount by cartViewModel
                    .cartCount()
                    .collectAsStateWithLifecycle(initialValue = 0)

                FavouriteFragmentScreen(
                    products = products,
                    isLoggedIn = isLoggedIn,
                    selectedIds = selectedIds,
                    allSelected = allSelected,
                    selectedCount = selectedCount,
                    onSelectAll = { selectAll ->

                        selectedIds = if (selectAll) {
                            products.map { it.productId }.toSet()
                        } else {
                            emptySet()
                        }
                    },
                    deleteSelected = {
                        val productsToDelete = products.filter {
                            it.productId in selectedIds
                        }

                        if (productsToDelete.isNotEmpty()) {
                            deleteAlertDialog(
                                products = productsToDelete,
                                onComplete = {
                                    selectedIds = emptySet()
                                }
                            )
                        }
                    },
                    onBackClick = {
                        requireActivity()
                            .onBackPressedDispatcher
                            .onBackPressed()
                    },
                    noOfItems = products.size,
                    cartCount = cartCount,
                    onCartClick = {
                        val intent = Intent(requireContext(), MainActivity::class.java).apply {
                            putExtra("open_fragment", "cart")
                            flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                        startActivity(intent)
                    },
                    continueShopping = {
                        if (isLoggedIn) {
                            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                                putExtra("open_fragment", "home")
                                flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            }
                            startActivity(intent)
                        } else {
                            val intent = Intent(requireContext(), LoginActivity::class.java)
                            startActivity(intent)
                        }
                    },
                    onProductClick = { product ->

                        selectedIds = if (product.productId in selectedIds) {
                            selectedIds - product.productId
                        } else {
                            selectedIds + product.productId
                        }
                    },
                    onAddToCartClick = { product ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            try {
                                cartViewModel.addToCartFromFavourite(product)
                                val coordinator = requireActivity().findViewById<View>(R.id.main)
                                val bottomNav = requireActivity().findViewById<View>(R.id.bottomNav)

                                SnackBarUtil.show(
                                    view = coordinator,
                                    context = requireContext(),
                                    text = "Added to cart successfully.",
                                    anchorView = bottomNav,
                                    actionText = "GO TO CART"
                                ) {
                                    val intent =
                                        Intent(requireContext(), MainActivity::class.java).apply {
                                            putExtra("open_fragment", "cart")
                                            flags =
                                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        }
                                    startActivity(intent)
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    requireContext(), "Failed to add product in cart.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                throw e
                            }
                        }
                    },
                    onOptionClick = {},
                    onTickClick = {},
                    onDeleteClick = { product ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            try {
                                favouriteViewModel.removeOne(product.productId)
                                val coordinator = requireActivity().findViewById<View>(R.id.main)
                                val bottomNav = requireActivity().findViewById<View>(R.id.bottomNav)

                                SnackBarUtil.show(
                                    view = coordinator,
                                    context = requireContext(),
                                    text = "(1) Item has been deleted.",
                                    anchorView = bottomNav,
                                    duration = 5000,
                                    actionText = "UNDO"
                                ) {
                                    viewLifecycleOwner.lifecycleScope.launch {
                                        favouriteViewModel.restoreFavourite(product)
                                    }
                                    SnackBarUtil.show(
                                        view = coordinator,
                                        context = requireContext(),
                                        text = "(1) Item restored successfully.",
                                        anchorView = bottomNav
                                    )
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to delete product.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                throw e
                            }
                        }
                    }
                )
            }
        }
    }

    private fun deleteAlertDialog(
        products: List<FavouriteResponse>,
        onComplete: () -> Unit
    ) {
        val titleView = TextView(requireContext()).apply {
            text = getString(R.string.alert_dialog_delete)
            textSize = 18f
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.text_dark
                )
            )
            setPadding(60, 60, 0, 0)
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setCustomTitle(titleView)
            .setNegativeButton("No", null)
            .setPositiveButton("Yes") { _, _ ->

                viewLifecycleOwner.lifecycleScope.launch {

                    try {
                        val deletedProducts = products.toList()

                        deletedProducts.forEach { product ->
                            favouriteViewModel.removeOne(product.productId)
                        }

                        val coordinator =
                            requireActivity().findViewById<View>(R.id.main)

                        val bottomNav =
                            requireActivity().findViewById<View>(R.id.bottomNav)

                        SnackBarUtil.show(
                            view = coordinator,
                            context = requireContext(),
                            text = "(${deletedProducts.size}) Items have been deleted.",
                            anchorView = bottomNav,
                            duration = 5000,
                            actionText = "UNDO"
                        ) {
                            viewLifecycleOwner.lifecycleScope.launch {

                                deletedProducts.forEach { product ->
                                    favouriteViewModel.restoreFavourite(product)
                                }

                                SnackBarUtil.show(
                                    view = coordinator,
                                    context = requireContext(),
                                    text = "(${deletedProducts.size}) Items restored successfully.",
                                    anchorView = bottomNav
                                )
                            }
                        }

                        onComplete()

                    } catch (e: Exception) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to delete selected products.",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.e(
                            "DELETE_FAILED",
                            "Failed to delete selected products.",
                            e
                        )
                    }
                }
            }
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
    }
}