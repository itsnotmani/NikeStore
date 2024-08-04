package com.example.nikestore.feature.order

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.common.convertDpToPixel
import com.example.nikestore.common.formatPrice
import com.example.nikestore.data.OrderHistoryItem
import com.example.nikestore.databinding.ItemOrderHistoryBinding
import com.example.nikestore.view.NikeImageView

class OrderHistoryItemAdapter(val context: Context, val orderHistoryItems: List<OrderHistoryItem>) :
    RecyclerView.Adapter<OrderHistoryItemAdapter.ViewHolder>() {
    val layoutParams: LinearLayout.LayoutParams

    init {
        val size = convertDpToPixel(100f, context).toInt()
        val margin = convertDpToPixel(8f, context).toInt()
        layoutParams = LinearLayout.LayoutParams(size, size)
        layoutParams.setMargins(margin, 0, margin, 0)
    }

    inner class ViewHolder(val binding: ItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(orderHistoryItem: OrderHistoryItem) {
            binding.orderIdTv.text = orderHistoryItem.id.toString()
            binding.orderAmountTv.text = formatPrice(orderHistoryItem.payable)
            binding.orderProductsLl.removeAllViews()
            orderHistoryItem.order_items.forEach {
                val imageView = NikeImageView(context)
                imageView.layoutParams = layoutParams
                imageView.setImageURI(it.product.image)
                binding.orderProductsLl.addView(imageView)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(orderHistoryItems[position])

    override fun getItemCount(): Int = orderHistoryItems.size
}