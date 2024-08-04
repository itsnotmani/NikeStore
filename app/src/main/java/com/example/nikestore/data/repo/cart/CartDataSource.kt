package com.example.nikestore.data.repo.cart

import com.example.nikestore.data.AddToCartResponse
import com.example.nikestore.data.CartItemCount
import com.example.nikestore.data.CartResponse
import com.example.nikestore.data.MessageResponse
import io.reactivex.Single

interface CartDataSource {

    fun addToCart(product: Int): Single<AddToCartResponse>

    fun get(): Single<CartResponse>
    fun remove(cartItemId: Int): Single<MessageResponse>
    fun  changeCount(cartItemId: Int,count: Int): Single<AddToCartResponse>
    fun getCartItemsCount():Single<CartItemCount>

}