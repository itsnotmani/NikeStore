package com.example.nikestore.data.repo.comment

import com.example.nikestore.data.Comment
import com.example.nikestore.services.http.ApiService
import io.reactivex.Single

class CommentRemoteDataSource(val apiService: ApiService): CommentDataSource {
    override fun getAll(productId:Int): Single<List<Comment>> = apiService.getComments(productId)

    override fun insert(): Single<Comment> {
        return super.insert()
    }
}