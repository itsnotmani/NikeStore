package com.example.nikestore.feature.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.data.Product
import com.example.nikestore.databinding.ActivityFavoriteProductsBinding
import com.example.nikestore.feature.favorites.FavoriteProductsAdapter
import com.example.nikestore.feature.product.ProductDetailActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class FavoriteProductsActivity : NikeActivity(), FavoriteProductsAdapter.FavoriteProductEventListener {

    private lateinit var binding: ActivityFavoriteProductsBinding
    val viewModel: FavoriteProductsViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.helpBtn.setOnClickListener {
            Snackbar.make(it, R.string.favorites_help_message, Snackbar.LENGTH_LONG).show()
        }

        viewModel.productsLiveData.observe(this) {
            if (it.isNotEmpty()) {
                binding.favoriteProductsRv.layoutManager =
                    LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                binding.favoriteProductsRv.adapter =
                    FavoriteProductsAdapter(it as MutableList<Product>, this, get())
            } else {
                showEmptyState(R.layout.view_default_empty_state)
                val emptyStateBinding = binding.root.findViewById<TextView>(R.id.emptyStateMessageTv)
                emptyStateBinding.text = getString(R.string.favorites_empty_state_message)
            }
        }

        binding.favoriteToolBar.onBackButtonClickListener = View.OnClickListener {
            finish()
        }
    }

    override fun onClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onLongClick(product: Product) {
        viewModel.removeFromFavorites(product)
    }


}

