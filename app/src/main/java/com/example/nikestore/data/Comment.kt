package com.example.nikestore.data

import com.sevenlearn.nikestore.data.Author

data class Comment(
    val author: Author,
    val content: String,
    val date: String,
    val id: Int,
    val title: String
)
