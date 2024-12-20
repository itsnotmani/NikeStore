package com.example.nikestore.feature.product.comments

import androidx.lifecycle.MutableLiveData
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.common.asyncNetworkRequest
import com.example.nikestore.data.Comment
import com.example.nikestore.data.repo.comment.CommentRepository

class CommentsListViewModel(product_Id : Int,commentRepository: CommentRepository) : NikeViewModel() {

    val commentsLiveData = MutableLiveData<List<Comment>>()

    init {
        progressBarLiveData.value = true
        commentRepository.getAll(product_Id)
            .asyncNetworkRequest()
            .doFinally{progressBarLiveData.value = false}
            .subscribe(object : NikeSingleObserver<List<Comment>>(compositeDisposable){
                override fun onSuccess(t: List<Comment>) {
                    commentsLiveData.value = t
                }

            })



    }

}