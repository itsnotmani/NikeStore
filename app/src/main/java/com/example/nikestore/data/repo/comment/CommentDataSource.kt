package com.example.nikestore.data.repo.comment

import com.example.nikestore.data.Comment
import com.example.nikestore.data.repo.comment.CommentRepository
import io.reactivex.Single

interface CommentDataSource: CommentRepository {

    override fun getAll(productId:Int): Single<List<Comment>> {
        TODO("Not yet implemented")
    }

    override fun insert(): Single<Comment> {
        TODO("Not yet implemented")
    }
}