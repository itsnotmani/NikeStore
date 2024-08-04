package com.example.nikestore.feature.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.common.formatPrice
import com.example.nikestore.data.CartItem
import com.example.nikestore.data.PurchseDetail
import com.example.nikestore.databinding.ItemCartBinding
import com.example.nikestore.databinding.ItemPurchaseDetailsBinding
import com.example.nikestore.services.ImageLoadingService

const val VIEW_TYPE_CART_ITEM = 0
const val VIEW_TYPE_PURCHASE_DETAILS = 1

class CartItemAdapter(
    val cartItems: MutableList<CartItem>,
    private val imageLoadingService: ImageLoadingService,
    private val cartItemViewCallBacks: CartItemViewCallBacks,
    var purchaseDetail: PurchseDetail? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CartItemViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindCartItem(cartItem: CartItem) {
            binding.productTitleTv.text = cartItem.product.title
            binding.cartItemCountTv.text = cartItem.count.toString()
            binding.previousPriceTv.text = formatPrice(cartItem.product.previous_price + cartItem.product.discount)
            binding.priceTv.text = formatPrice(cartItem.product.price)
            imageLoadingService.load(binding.productIv, cartItem.product.image)

            binding.removeFromCartBtn.setOnClickListener {
                cartItemViewCallBacks.onRemoveCartItemButtonClick(cartItem)
            }

            binding.changeCountProgressBar.visibility =
                if (cartItem.changeCountProgressBarIsVisible) View.VISIBLE else View.GONE

            binding.cartItemCountTv.visibility =
                if (cartItem.changeCountProgressBarIsVisible) View.INVISIBLE else View.VISIBLE

            binding.cartItemCountTv.visibility=if (cartItem.changeCountProgressBarIsVisible) View.INVISIBLE else View.VISIBLE
            binding.increaseBtn.setOnClickListener {
                cartItem.changeCountProgressBarIsVisible = true
                binding.changeCountProgressBar.visibility = View.VISIBLE
                binding.cartItemCountTv.visibility = View.INVISIBLE
                cartItemViewCallBacks.onIncreaseCartItemButtonClick(cartItem)
            }

            binding.decreaseBtn.setOnClickListener {
                if (cartItem.count > 1) {
                    cartItem.changeCountProgressBarIsVisible = true
                    binding.changeCountProgressBar.visibility = View.VISIBLE
                    binding.cartItemCountTv.visibility = View.INVISIBLE
                    cartItemViewCallBacks.onDecreaseCartItemButtonClick(cartItem)
                }
            }

            binding.productIv.setOnClickListener {
                cartItemViewCallBacks.onProductImageClick(cartItem)
            }

            if (cartItem.changeCountProgressBarIsVisible) {
                binding.changeCountProgressBar.visibility = View.VISIBLE
                binding.increaseBtn.visibility = View.GONE
            }
        }
    }

    class PurchaseDetailViewHolder(private val binding: ItemPurchaseDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(totalPrice: Int, shippingCost: Int, payablePrice: Int) {
            binding.totalPriceTv.text = formatPrice(totalPrice)
            binding.shippingCostTv.text = formatPrice(shippingCost)
            binding.payablePriceTv.text = formatPrice(payablePrice)
        }
    }

    interface CartItemViewCallBacks {
        fun onRemoveCartItemButtonClick(cartItem: CartItem)
        fun onIncreaseCartItemButtonClick(cartItem: CartItem)
        fun onDecreaseCartItemButtonClick(cartItem: CartItem)
        fun onProductImageClick(cartItem: CartItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CART_ITEM) {
            val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CartItemViewHolder(binding)
        } else {
            val binding = ItemPurchaseDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            PurchaseDetailViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = cartItems.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartItemViewHolder) {
            holder.bindCartItem(cartItems[position])
        } else if (holder is PurchaseDetailViewHolder) {
            purchaseDetail?.let {
                holder.bind(it.total_price.toInt(), it.shipping_cost.toInt(), it.payable_price.toInt())
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == cartItems.size) VIEW_TYPE_PURCHASE_DETAILS else VIEW_TYPE_CART_ITEM
    }

    fun removeCartItem(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun increaseCount(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems[index].changeCountProgressBarIsVisible = false
            notifyItemChanged(index)
        }
    }

    fun decreaseCount(cartItem: CartItem){
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems[index].changeCountProgressBarIsVisible = false
            notifyItemChanged(index)
        }
    }
}
