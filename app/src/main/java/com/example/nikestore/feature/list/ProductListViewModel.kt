package com.example.nikestore.feature.list

import androidx.lifecycle.MutableLiveData
import com.example.nikestore.R
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.common.asyncNetworkRequest
import com.example.nikestore.data.Product
import com.example.nikestore.data.repo.product.ProductRepository

class ProductListViewModel (var sort:Int, val productRepository: ProductRepository):NikeViewModel() {

    val productsLiveData = MutableLiveData<List<Product>>()
    val selectedSortTiltleLiveData = MutableLiveData<Int>()
    val sortTitles = arrayOf(R.string.sortLatest,R.string.sortPopular,R.string.sortPriceHighToLow,R.string.sortPriceLowToHigh)

    init {
        getProducts()
        selectedSortTiltleLiveData.value = sortTitles[sort]
    }

    fun getProducts(){

        progressBarLiveData.value = true

        productRepository.getProducts(sort)
            .asyncNetworkRequest()
            .doFinally{progressBarLiveData.value = false}
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    productsLiveData.value = t
                }

            })
    }

    fun onSelectedSortChangedByUser(sort: Int){

        this.sort = sort
        this.selectedSortTiltleLiveData.value = sortTitles[sort]
        getProducts()

    }

}