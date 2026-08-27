package com.example.eSewaMarket

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.eSewaMarket.data.models.NavItem
import com.example.eSewaMarket.databinding.ActivityMainBinding
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.fragments.CartFragment
import com.example.eSewaMarket.ui.fragments.FavouriteFragment
import com.example.eSewaMarket.ui.fragments.HomeFragment
import com.example.eSewaMarket.ui.fragments.MoreFragment
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var selectedTab = 1
    private val tabHistory = ArrayDeque<Int>()

    private lateinit var shop: NavItem
    private lateinit var cart: NavItem
    private lateinit var favourite: NavItem
    private lateinit var more: NavItem

    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactoryProvider.cartFactory(this)
    }

    private val favouriteViewModel: FavouriteViewModel by viewModels {
        ViewModelFactoryProvider.favouriteFactory(this)
    }

    private companion object {
        const val HOME_TAG = "HOME"
        const val CART_TAG = "CART"
        const val FAVOURITE_TAG = "FAVOURITE"
        const val MORE_TAG = "MORE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1000)

        installSplashScreen()
        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        setupNavigationItems()
        setupBottomNavigation()

        if (savedInstanceState == null) {
            showInitialHome()
        } else {
            restoreCurrentTab()
        }

        handleIntent(intent)
        setupBackNavigation()
        observeCartCount()
        observeFavouriteCount()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNav.root) { view, insets ->

            val bottom = insets.getInsets(
                WindowInsetsCompat.Type.navigationBars()
            ).bottom

            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                bottom
            )

            insets
        }
    }

    private fun setupNavigationItems() {

        shop = NavItem(
            binding.bottomNav.shopButton,
            binding.bottomNav.shopLabel,
            binding.bottomNav.shopIcon
        )

        cart = NavItem(
            binding.bottomNav.cartButton,
            binding.bottomNav.cartLabel,
            binding.bottomNav.cartIcon
        )

        favourite = NavItem(
            binding.bottomNav.favouriteButton,
            binding.bottomNav.favouriteLabel,
            binding.bottomNav.favouriteIcon
        )

        more = NavItem(
            binding.bottomNav.moreButon,
            binding.bottomNav.moreLabel,
            binding.bottomNav.moreIcon
        )
    }

    private fun setupBottomNavigation() {

        binding.bottomNav.shopButton.setOnClickListener {

            if (selectedTab != 1) {
                navigateToTab(
                    tag = HOME_TAG,
                    selectedItem = shop,
                    deselectedItems = arrayOf(cart, favourite, more),
                    tab = 1
                )
            }
        }

        binding.bottomNav.cartButton.setOnClickListener {

            if (selectedTab != 2) {
                navigateToTab(
                    tag = CART_TAG,
                    selectedItem = cart,
                    deselectedItems = arrayOf(shop, favourite, more),
                    tab = 2
                )
            }
        }

        binding.bottomNav.favouriteButton.setOnClickListener {

            if (selectedTab != 3) {
                navigateToTab(
                    tag = FAVOURITE_TAG,
                    selectedItem = favourite,
                    deselectedItems = arrayOf(shop, cart, more),
                    tab = 3
                )
            }
        }

        binding.bottomNav.moreButon.setOnClickListener {

            if (selectedTab != 4) {
                navigateToTab(
                    tag = MORE_TAG,
                    selectedItem = more,
                    deselectedItems = arrayOf(shop, cart, favourite),
                    tab = 4
                )
            }
        }
    }

    private fun showInitialHome() {

        val home = HomeFragment()

        supportFragmentManager.beginTransaction()
            .add(
                R.id.mainFrame,
                home,
                HOME_TAG
            )
            .commit()

        selectedTab = 1
        onSelect(shop)
        onDeselect(cart)
        onDeselect(favourite)
        onDeselect(more)
    }

    private fun restoreCurrentTab() {

        val home = supportFragmentManager.findFragmentByTag(HOME_TAG)

        val cartFragment = supportFragmentManager.findFragmentByTag(CART_TAG)

        val favouriteFragment =
            supportFragmentManager.findFragmentByTag(FAVOURITE_TAG)

        val moreFragment =
            supportFragmentManager.findFragmentByTag(MORE_TAG)

        when {
            home?.isVisible == true -> {
                selectedTab = 1
                updateSelectedTab(shop, cart, favourite, more)
            }

            cartFragment?.isVisible == true -> {
                selectedTab = 2
                updateSelectedTab(cart, shop, favourite, more)
            }

            favouriteFragment?.isVisible == true -> {
                selectedTab = 3
                updateSelectedTab(favourite, shop, cart, more)
            }

            moreFragment?.isVisible == true -> {
                selectedTab = 4
                updateSelectedTab(more, shop, cart, favourite)
            }

            else -> {

                showTab(
                    tag = HOME_TAG,
                    selectedItem = shop,
                    deselectedItems = arrayOf(cart, favourite, more),
                    tab = 1
                )
            }
        }
    }

    private fun navigateToTab(
        tag: String,
        selectedItem: NavItem,
        deselectedItems: Array<NavItem>,
        tab: Int
    ) {

        if (selectedTab == tab) return

        tabHistory.addLast(selectedTab)

        showTab(
            tag = tag,
            selectedItem = selectedItem,
            deselectedItems = deselectedItems,
            tab = tab
        )
    }

    private fun showTab(
        tag: String,
        selectedItem: NavItem,
        deselectedItems: Array<NavItem>,
        tab: Int
    ) {

        if (selectedTab == tab && getFragmentByTag(tag)?.isVisible == true) {
            return
        }

        val fragmentManager = supportFragmentManager

        val targetFragment = getFragmentByTag(tag)

        val currentFragment = getCurrentFragment()

        val transaction = fragmentManager.beginTransaction()

        if (currentFragment != null && currentFragment !== targetFragment) {
            transaction.hide(currentFragment)
        }

        if (targetFragment == null) {

            val newFragment = createFragmentForTag(tag)

            transaction.add(
                R.id.mainFrame,
                newFragment,
                tag
            )

        } else {
            transaction.show(targetFragment)
        }

        transaction.commit()

        selectedTab = tab

        updateSelectedTab(
            selectedItem,
            *deselectedItems
        )
    }

    private fun createFragmentForTag(tag: String): Fragment {
        return when (tag) {

            HOME_TAG -> HomeFragment()

            CART_TAG -> CartFragment()

            FAVOURITE_TAG -> FavouriteFragment()

            MORE_TAG -> MoreFragment()

            else -> throw IllegalArgumentException(
                "Unknown fragment tag: $tag"
            )
        }
    }

    private fun getFragmentByTag(tag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(tag)
    }

    private fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.fragments.firstOrNull {
            it.isVisible
        }
    }

    private fun updateSelectedTab(
        selectedItem: NavItem,
        vararg deselectedItems: NavItem
    ) {

        onSelect(selectedItem)

        deselectedItems.forEach {
            onDeselect(it)
        }
    }

    private fun onSelect(item: NavItem) {

        item.label.visibility = View.VISIBLE

        item.icon.imageTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    this,
                    R.color.green
                )
            )

        item.button.setBackgroundResource(
            R.drawable.navigation_background
        )

        item.button.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(100)
            .withEndAction {

                item.button.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
            }
    }

    private fun onDeselect(item: NavItem) {

        item.label.visibility = View.GONE

        item.icon.imageTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            )

        item.button.setBackgroundResource(
            android.R.color.transparent
        )
    }

    private fun setupBackNavigation() {

        onBackPressedDispatcher.addCallback(this) {

            if (tabHistory.isNotEmpty()) {

                val previousTab = tabHistory.removeLast()

                when (previousTab) {

                    1 -> showTab(
                        tag = HOME_TAG,
                        selectedItem = shop,
                        deselectedItems = arrayOf(
                            cart,
                            favourite,
                            more
                        ),
                        tab = 1
                    )

                    2 -> showTab(
                        tag = CART_TAG,
                        selectedItem = cart,
                        deselectedItems = arrayOf(
                            shop,
                            favourite,
                            more
                        ),
                        tab = 2
                    )

                    3 -> showTab(
                        tag = FAVOURITE_TAG,
                        selectedItem = favourite,
                        deselectedItems = arrayOf(
                            shop,
                            cart,
                            more
                        ),
                        tab = 3
                    )

                    4 -> showTab(
                        tag = MORE_TAG,
                        selectedItem = more,
                        deselectedItems = arrayOf(
                            shop,
                            cart,
                            favourite
                        ),
                        tab = 4
                    )
                }

            } else {
                finish()
            }
        }
    }

    private fun handleIntent(intent: Intent?) {

        if (intent?.getBooleanExtra("login_success", false) == true) {
            Toast.makeText(
                this,
                "Login Successful",
                Toast.LENGTH_SHORT
            ).show()
        }

        when (intent?.getStringExtra("open_fragment")) {

            "home" -> {

                navigateToTab(
                    tag = HOME_TAG,
                    selectedItem = shop,
                    deselectedItems = arrayOf(
                        cart,
                        favourite,
                        more
                    ),
                    tab = 1
                )
            }

            "cart" -> {

                navigateToTab(
                    tag = CART_TAG,
                    selectedItem = cart,
                    deselectedItems = arrayOf(
                        shop,
                        favourite,
                        more
                    ),
                    tab = 2
                )
            }

            "favourite" -> {

                navigateToTab(
                    tag = FAVOURITE_TAG,
                    selectedItem = favourite,
                    deselectedItems = arrayOf(
                        shop,
                        cart,
                        more
                    ),
                    tab = 3
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)

        handleIntent(intent)
    }

    private fun observeCartCount() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                cartViewModel.cartCount().collect { count ->

                    if (count > 0) {

                        binding.bottomNav.numOfProductInCart.text =
                            count.toString()

                        binding.bottomNav.numOfProductInCart.visibility =
                            View.VISIBLE

                    } else {

                        binding.bottomNav.numOfProductInCart.visibility =
                            View.GONE
                    }
                }
            }
        }
    }

    private fun observeFavouriteCount() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                favouriteViewModel.favouriteCount().collect { count ->

                    binding.bottomNav.numOfProductInFavourite.visibility =
                        if (count > 0) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }

                    binding.bottomNav.numOfProductInFavourite.text =
                        count.toString()
                }
            }
        }
    }
}