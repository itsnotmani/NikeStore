package com.example.nikestore.data.repo.user

import com.example.nikestore.data.MessageResponse
import com.example.nikestore.data.TokenResponse
import io.reactivex.Single

interface UserDataSource {

    fun login(username: String,password: String): Single<TokenResponse>
    fun signup(username: String,password: String): Single<MessageResponse>
    fun loadToken()
    fun saveToken(token:String,refreshToken: String)

    fun saveUsername(username: String)
    fun getUsername(): String
    fun signOut()

}