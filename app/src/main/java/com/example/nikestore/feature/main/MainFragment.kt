package com.example.nikestore.feature.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeFragment
import com.example.nikestore.common.convertDpToPixel
import com.example.nikestore.data.Product
import com.example.nikestore.data.SORT_LATEST
import com.example.nikestore.data.SORT_POPULAR
import com.example.nikestore.databinding.FragmentMainBinding
import com.example.nikestore.feature.list.ProductListActivity
import com.example.nikestore.feature.product.ProductDetailActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class MainFragment : NikeFragment(), ProductListAdapter.ProductEventListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModel()

    // به درستی تعریف پارامترها
    private val productListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }
    private val bestSellingProductListAdapter: ProductListAdapter by inject { parametersOf(
        VIEW_TYPE_ROUND) } // فرض بر این است که نوع دیگری برای Best Selling Products نیاز است

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lastProductRv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.lastProductRv.adapter = productListAdapter
        productListAdapter.productEventListener = this

        binding.papularProductRv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.papularProductRv.adapter = bestSellingProductListAdapter
        bestSellingProductListAdapter.productEventListener = this

        mainViewModel.productsLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            productListAdapter.product = it as ArrayList<Product>
        }

        mainViewModel.papularProducts.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            bestSellingProductListAdapter.product = it as ArrayList<Product>
        }

        binding.viewAllLatestProductBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, SORT_LATEST)
            })
        }

        binding.viewAllPapularProductBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, SORT_POPULAR)
            })
        }

        mainViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        mainViewModel.bannersLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())

            val bannerSliderAdapter = BannerSliderAdapter(this, it)
            binding.bannerSliderViewPager.adapter = bannerSliderAdapter
            val viewPagerHeight = ((binding.bannerSliderViewPager.measuredWidth - convertDpToPixel(32f, requireContext())) * 173 / 328).toInt()
            val layoutParams = binding.bannerSliderViewPager.layoutParams
            layoutParams.height = viewPagerHeight
            binding.bannerSliderViewPager.layoutParams = layoutParams
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onFavoriteBtnClick(product: Product) {
        mainViewModel.addProductToFavorites(product)
    }
}
