package com.example.nikestore.feature.main

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.formatPrice
import com.example.nikestore.common.implementSpringAnimationTrait
import com.example.nikestore.data.Product
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.view.NikeImageView

const val VIEW_TYPE_SMALL = 1
const val VIEW_TYPE_ROUND = 0
const val VIEW_TYPE_LARGE = 2

class ProductListAdapter(
    var viewType: Int,
    private val imageLoadingService: ImageLoadingService
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    var productEventListener: ProductEventListener? = null
    var product = ArrayList<Product>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV: TextView = itemView.findViewById(R.id.productTitleTv)
        val productIv: NikeImageView = itemView.findViewById(R.id.productIv)
        val currentPriceTv: TextView = itemView.findViewById(R.id.currentPriceTv)
        val previousPriceTv: TextView = itemView.findViewById(R.id.previousPriceTv)
        val favoriteBtn: ImageView = itemView.findViewById(R.id.favoriteBtn)

        fun bindProduct(product: Product) {
            imageLoadingService.load(productIv, product.image)
            titleTV.text = product.title
            currentPriceTv.text = formatPrice(product.price)
            previousPriceTv.text = formatPrice(product.previous_price)
            previousPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                productEventListener?.onProductClick(product)
            }

            if (product.isFavorite) {
                favoriteBtn.setImageResource(R.drawable.ic_favorite_fill)
            } else {
                favoriteBtn.setImageResource(R.drawable.ic_favorites)
            }

            favoriteBtn.setOnClickListener {
                productEventListener?.onFavoriteBtnClick(product)
                product.isFavorite = !product.isFavorite
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutResId = when (viewType) {
            VIEW_TYPE_ROUND -> R.layout.item_product
            VIEW_TYPE_SMALL -> R.layout.item_product_small
            VIEW_TYPE_LARGE -> R.layout.item_product_large
            else -> throw IllegalStateException("View type is not valid")
        }
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false))
    }

    override fun getItemCount(): Int = product.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindProduct(product[position])
    }

    interface ProductEventListener {
        fun onProductClick(product: Product)
        fun onFavoriteBtnClick(product: Product)
    }
}
