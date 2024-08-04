package com.example.nikestore.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.nikestore.R
import com.example.nikestore.databinding.ViewToolbarBinding

class NikeToolbar(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    var onBackButtonClickListener: View.OnClickListener? = null
        set(value) {
            field = value
            binding.backBtn.setOnClickListener(onBackButtonClickListener)
        }

    private val binding: ViewToolbarBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewToolbarBinding.inflate(inflater, this, true)

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.NikeToolbar)
            val title = a.getString(R.styleable.NikeToolbar_nt_title)
            if (title != null && title.isNotEmpty())
                binding.toolbarTitleTv.text = title

            a.recycle()
        }
    }
}
