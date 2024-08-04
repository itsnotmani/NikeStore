package com.example.nikestore.feature.main

import androidx.lifecycle.MutableLiveData
import com.example.nikestore.common.NikeCompletableObserver
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.data.Banner
import com.example.nikestore.data.Product
import com.example.nikestore.data.SORT_LATEST
import com.example.nikestore.data.SORT_POPULAR
import com.example.nikestore.data.repo.banner.BannerRepository
import com.example.nikestore.data.repo.product.ProductRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val productRepository: ProductRepository,
    bannerRepository: BannerRepository
) : NikeViewModel() {
    val productsLiveData = MutableLiveData<List<Product>>()
    val bannersLiveData = MutableLiveData<List<Banner>>()
    val papularProducts = MutableLiveData<List<Product>>()

    init {
        progressBarLiveData.value = true

        // بارگذاری محصولات جدید
        productRepository.getProducts(SORT_LATEST)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    productsLiveData.value = t
                }
            })

        // بارگذاری بنرها
        bannerRepository.getBanners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    bannersLiveData.value = t
                }
            })

        // بارگذاری پرفروش‌ترین محصولات
        loadBestSellingProducts()
    }

    private fun loadBestSellingProducts() {
        productRepository.getProducts(SORT_POPULAR)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    papularProducts.value = t
                }
            })
    }

    fun addProductToFavorites(product: Product) {
        if (product.isFavorite)
            productRepository.deleteFromFavorites(product)
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite = false
                    }
                })
        else
            productRepository.addToFavorites(product)
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite = true
                    }
                })
    }
}
