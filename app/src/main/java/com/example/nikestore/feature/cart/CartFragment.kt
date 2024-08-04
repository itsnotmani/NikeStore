package com.example.nikestore.feature.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeCompletableObserver
import com.example.nikestore.common.NikeFragment
import com.example.nikestore.data.CartItem
import com.example.nikestore.databinding.FragmentCartBinding
import com.example.nikestore.databinding.ViewCartEmptyStateBinding
import com.example.nikestore.feature.auth.AuthActivity
import com.example.nikestore.feature.product.ProductDetailActivity
import com.example.nikestore.feature.shipping.ShippingActivity
import com.example.nikestore.services.ImageLoadingService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CartFragment: NikeFragment(), CartItemAdapter.CartItemViewCallBacks {

    private val cartViewModel: CartViewModel by viewModel()
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    var cartItemAdapter: CartItemAdapter? = null
    val imageLoadingService: ImageLoadingService by inject()
    val compositeDisposable = CompositeDisposable()
    private var emptyStateBinding: ViewCartEmptyStateBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        emptyStateBinding = ViewCartEmptyStateBinding.bind(binding.emptyStateContainer.getChildAt(0))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        cartViewModel.cartItemsLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            binding.cartItemsRv.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            cartItemAdapter =
                CartItemAdapter(it.toMutableList(), imageLoadingService, this)
            binding.cartItemsRv.adapter = cartItemAdapter
            updatePayButtonVisibility(it.isNotEmpty())
        }

        cartViewModel.purchaseDetailLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            cartItemAdapter?.let { adapter ->
                adapter.purchaseDetail = it
                adapter.notifyItemChanged(adapter.cartItems.size)
            }
        }

        cartViewModel.emptyStateLiveData.observe(viewLifecycleOwner) { emptyState ->
            if (emptyState.mustShow) {
                binding.emptyStateContainer.visibility = View.VISIBLE
                binding.payBtn.visibility = View.GONE // Hide pay button if empty state is shown

                emptyStateBinding?.let { binding ->
                    binding.emptyStateMessageTv.text = getString(emptyState.messageResId)
                    binding.emptyStateCtaBtn.visibility =
                        if (emptyState.mustShowCallToActionButton) View.VISIBLE else View.GONE
                    binding.emptyStateCtaBtn.setOnClickListener {
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                    }
                }
            } else {
                binding.emptyStateContainer.visibility = View.GONE
                updatePayButtonVisibility(cartViewModel.cartItemsLiveData.value?.isNotEmpty() == true)
            }
        }

        binding.payBtn.setOnClickListener(){
            startActivity(Intent(requireContext(),ShippingActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, cartViewModel.purchaseDetailLiveData.value)
            })
        }
    }



    private fun updatePayButtonVisibility(isCartNotEmpty: Boolean) {
        binding.payBtn.visibility = if (isCartNotEmpty) View.VISIBLE else View.GONE
    }

    override fun onStart() {
        super.onStart()
        cartViewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        emptyStateBinding = null
    }

    override fun onRemoveCartItemButtonClick(cartItem: CartItem) {
        cartViewModel.removeItemFromCart(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    cartItemAdapter?.removeCartItem(cartItem)
                }


            })
    }


    override fun onIncreaseCartItemButtonClick(cartItem: CartItem) {
        cartViewModel.increaseCartItemCount(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    cartItemAdapter?.increaseCount(cartItem)
                }
            })
    }

    override fun onDecreaseCartItemButtonClick(cartItem: CartItem) {
        cartViewModel.decreaseCartItemCount(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    cartItemAdapter?.decreaseCount(cartItem)
                }
            })
    }

    override fun onProductImageClick(cartItem: CartItem) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, cartItem.product)
        })
    }
}
