package com.example.nikestore.feature.main

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.nikestore.R
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.common.convertDpToPixel
import com.example.nikestore.common.setupWithNavController
import com.example.nikestore.data.CartItemCount
import com.example.nikestore.databinding.ActivityMainBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject

class MainActivity : NikeActivity() {
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationMain

        val navGraphIds = listOf(R.navigation.home, R.navigation.cart, R.navigation.profile)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCartItemCountChangeEvent(cartItemCount: CartItemCount) {
        val badge = binding.bottomNavigationMain.getOrCreateBadge(R.id.cart)
        badge.badgeGravity = BadgeDrawable.BOTTOM_START
        badge.backgroundColor = MaterialColors.getColor(binding.bottomNavigationMain,
            org.koin.android.R.attr.colorPrimary)
        badge.number = cartItemCount.count
        badge.verticalOffset = convertDpToPixel(10f, this).toInt()
        badge.isVisible = cartItemCount.count > 0
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCartItemsCount()
    }
}
