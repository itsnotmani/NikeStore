package com.example.nikestore.feature.product.comments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.common.EXTRA_KEY_ID
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.data.Comment
import com.example.nikestore.databinding.ActivityCommentListBinding
import com.example.nikestore.feature.product.CommentAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentListActivity : NikeActivity() {
    private val viewModel: CommentsListViewModel by viewModel {
        parametersOf(intent.extras!!.getInt(EXTRA_KEY_ID))
    }
    private lateinit var binding: ActivityCommentListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the binding and set the content view
        binding = ActivityCommentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe the LiveData for showing/hiding the progress indicator
        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        // Observe the LiveData for comments and update the RecyclerView
        viewModel.commentsLiveData.observe(this) { comments ->
            val adapter = CommentAdapter(true)
            binding.commentsRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            adapter.comments = comments as ArrayList<Comment>
            binding.commentsRv.adapter = adapter
        }

        // Set up the back button click listener
        binding.commentListToolbar.onBackButtonClickListener = View.OnClickListener {
            finish()
        }
    }
}
