package com.example.nikestore.feature.shipping

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.EXTRA_KEY_ID
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.openUrlInCustomTab
import com.example.nikestore.data.PurchseDetail
import com.example.nikestore.data.SubmitOrderResult
import com.example.nikestore.databinding.ActivityShippingBinding
import com.example.nikestore.feature.cart.CartItemAdapter
import com.example.nikestore.feature.checkout.CheckoutActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShippingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShippingBinding


    val viewModel:ShippingViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShippingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val purchaseDetail = intent.getParcelableExtra<PurchseDetail>(EXTRA_KEY_DATA)
            ?: throw IllegalStateException("purchase detail cannot be null")

        val viewHolder = CartItemAdapter.PurchaseDetailViewHolder(binding.purchaseDetailView)
        viewHolder.bind(
            purchaseDetail.total_price,
            purchaseDetail.shipping_cost,
            purchaseDetail.payable_price
        )

        val onClickListener = View.OnClickListener {
            viewModel.submitOrder(
                binding.firstNameEt.text.toString(),
                binding.lastNameEt.text.toString(),
                binding.postalCodeEt.text.toString(),
                binding.phoneNumberEt.text.toString(),
                binding.addressEt.text.toString(),
                if (it.id == R.id.onlinePaymentBtn) PAYMENT_METHOD_ONLINE else PAYMENT_METHOD_COD
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikeSingleObserver<SubmitOrderResult>(compositeDisposable) {
                    override fun onSuccess(t: SubmitOrderResult) {
                        if (t.bank_gateway_url.isNotEmpty()) {
                            openUrlInCustomTab(this@ShippingActivity, t.bank_gateway_url)
                        } else {
                            startActivity(
                                Intent(
                                    this@ShippingActivity,
                                    CheckoutActivity::class.java
                                ).apply {
                                    putExtra(EXTRA_KEY_ID, t.order_id)
                                }
                            )
                        }
                        finish()

                    }
                })


        }
        binding.shippingTollBar.setOnClickListener(){
            finish()
        }
        binding.onlinePaymentBtn.setOnClickListener(onClickListener)
        binding.codBtn.setOnClickListener(onClickListener)

    }

}
