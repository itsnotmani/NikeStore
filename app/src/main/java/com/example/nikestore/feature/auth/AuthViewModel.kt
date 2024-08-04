package com.example.nikestore.feature.auth

import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.data.repo.user.UserRepository
import io.reactivex.Completable

class AuthViewModel(private val userRepository: UserRepository) : NikeViewModel() {

    fun login(email: String,password:String): Completable{
        progressBarLiveData.value = true
        return userRepository.login(email,password).doFinally(){
            progressBarLiveData.value = false
        }
    }

    fun signup(email: String,password: String): Completable{
        progressBarLiveData.value = true
        return userRepository.signup(email,password).doFinally(){
            progressBarLiveData.postValue(false)
        }
    }

}