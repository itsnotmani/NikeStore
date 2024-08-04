package com.example.nikestore.data.repo.user

import com.example.nikestore.data.TokenContainer
import com.example.nikestore.data.TokenResponse
import io.reactivex.Completable

class UserRepositoryImpl(
    val userLocalDataSource: UserDataSource,
    val userRemoteDataSource: UserDataSource
) : UserRepository {
    override fun login(username: String, password: String): Completable {
        return userRemoteDataSource.login(username, password).doOnSuccess {
            onSuccessfulLogin(username,it)
        }.ignoreElement()
    }

    override fun signup(username: String, password: String): Completable {
        return userRemoteDataSource.signup(username,password).flatMap {
            userLocalDataSource.login(username,password)
        }.doOnSuccess(){
            onSuccessfulLogin(username,it)
        }.ignoreElement()
    }

    override fun loadToken() {
        userLocalDataSource.loadToken()
    }

    override fun getUserName(): String = userLocalDataSource.getUsername()

    override fun signOut() {
        userLocalDataSource.signOut()
        TokenContainer.update(null,null)
    }

    fun onSuccessfulLogin(username: String,tokenResponse: TokenResponse) {
        TokenContainer.update(tokenResponse.access_token, tokenResponse.refresh_token)
        userLocalDataSource.saveToken(tokenResponse.access_token, tokenResponse.refresh_token)
        userLocalDataSource.saveUsername(username)
    }
}