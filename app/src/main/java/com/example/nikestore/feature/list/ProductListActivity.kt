package com.example.nikestore.feature.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.data.Product
import com.example.nikestore.databinding.ActivityProductListBinding
import com.example.nikestore.feature.main.ProductListAdapter
import com.example.nikestore.feature.main.VIEW_TYPE_LARGE
import com.example.nikestore.feature.main.VIEW_TYPE_SMALL
import com.example.nikestore.feature.product.ProductDetailActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ProductListActivity : NikeActivity(),ProductListAdapter.ProductEventListener {
    private val viewModel: ProductListViewModel by viewModel { parametersOf(intent.extras!!.getInt(EXTRA_KEY_DATA)) }
    private val productListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_SMALL) }
    private lateinit var binding: ActivityProductListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // مقداردهی binding
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.productsRv.layoutManager = gridLayoutManager
        binding.productsRv.adapter = productListAdapter

        productListAdapter.productEventListener = this


        binding.viewTypeChangerBtn.setOnClickListener(){
            if (productListAdapter.viewType == VIEW_TYPE_SMALL ){
                binding.viewTypeChangerBtn.setImageResource(R.drawable.ic_view_type_large)
                productListAdapter.viewType = VIEW_TYPE_LARGE
                gridLayoutManager.spanCount = 1
                productListAdapter.notifyDataSetChanged()
            }else{
                binding.viewTypeChangerBtn.setImageResource(R.drawable.ic_grid)


                productListAdapter.viewType = VIEW_TYPE_SMALL
                gridLayoutManager.spanCount = 2
                productListAdapter.notifyDataSetChanged()
            }
        }

        viewModel.selectedSortTiltleLiveData.observe(this){
            binding.selectedSortTitleTv.text = getString(it)
        }

        viewModel.progressBarLiveData.observe(this){
            setProgressIndicator(it)
        }

        binding.sortBtn.setOnClickListener(){
            val dialog = MaterialAlertDialogBuilder(this)
                .setSingleChoiceItems(R.array.sortTitlesArray,viewModel.sort
                ) { dialog, which ->
                    dialog.dismiss()
                    viewModel.onSelectedSortChangedByUser(which)
                }.setTitle(getString(R.string.sort))

            dialog.show()
        }


        viewModel.productsLiveData.observe(this) {
            Timber.i(it.toString())
            productListAdapter.product = it as ArrayList<Product>
        }

        binding.toolbarView.onBackButtonClickListener = View.OnClickListener {
            finish()
        }
    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(this,ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA,product)
        })
    }

    override fun onFavoriteBtnClick(product: Product) {
        TODO("Not yet implemented")
    }
}
