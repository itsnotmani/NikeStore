package com.example.nikestore.feature.product

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_ID
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.common.NikeCompletableObserver
import com.example.nikestore.common.formatPrice
import com.example.nikestore.data.Comment
import com.example.nikestore.databinding.ActivityProductDetailBinding
import com.example.nikestore.feature.ProductDetailViewModel
import com.example.nikestore.feature.product.comments.CommentListActivity
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.view.scroll.ObservableScrollViewCallbacks
import com.example.nikestore.view.scroll.ScrollState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ProductDetailActivity : NikeActivity() {
    val productDetailViewModel: ProductDetailViewModel by viewModel { parametersOf(intent.extras) }
    val imageLoadingService: ImageLoadingService by inject()
    val commentAdapter = CommentAdapter()
    private lateinit var binding: ActivityProductDetailBinding
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productDetailViewModel.productLiveData.observe(this) {
            imageLoadingService.load(binding.productIv, it.image)
            binding.titleTv.text = it.title
            binding.previousPriceTv.text = formatPrice(it.previous_price)
            binding.previousPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.currentPriceTv.text = formatPrice(it.price)
            binding.toolbarTitleTv.text = it.title
        }

        productDetailViewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        productDetailViewModel.commentsLiveData.observe(this) {
            Timber.i(it.toString())
            commentAdapter.comments = it as ArrayList<Comment>
            if(it.size>3){

                binding.viewAllCommentsBtn.visibility = View.VISIBLE

                binding.viewAllCommentsBtn.setOnClickListener(){
                    startActivity(Intent(this, CommentListActivity::class.java).apply {
                        putExtra(EXTRA_KEY_ID,productDetailViewModel.productLiveData.value!!.id)
                    })
                }


            }
        }

        initViews()
    }

    private fun initViews() {
        binding.commentsRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.commentsRv.adapter = commentAdapter
        binding.commentsRv.isNestedScrollingEnabled = false


        binding.productIv.post {
            val productIvHeight = binding.productIv.height
            val toolbar = binding.toolbarView
            val productImageView = binding.productIv
            val compositeDisposable = CompositeDisposable()
            binding.observableScrollView.addScrollViewCallbacks(object : ObservableScrollViewCallbacks {
                override fun onScrollChanged(
                    scrollY: Int,
                    firstScroll: Boolean,
                    dragging: Boolean
                ) {
                    Timber.i("productIv height is -> $productIvHeight")
                    toolbar.alpha = scrollY.toFloat() / productIvHeight.toFloat()
                    productImageView.translationY = scrollY.toFloat() / 2
                }

                override fun onDownMotionEvent() {}
                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {}
            })
        }

        binding.backBtn.setOnClickListener(){
            finish()
        }

        binding.addToCartBtn.setOnClickListener(){
            productDetailViewModel.onAddToCartBtn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        showSnackBar(getString(R.string.successAddToCart))
                    }

                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
