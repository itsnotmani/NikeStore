package com.example.nikestore

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.example.nikestore.data.db.AppDataBase
import com.example.nikestore.data.repo.banner.BannerRepository
import com.example.nikestore.data.repo.banner.BannerRepositoryImpl
import com.example.nikestore.data.repo.cart.CartRepository
import com.example.nikestore.data.repo.cart.CartRepositoryImpl
import com.example.nikestore.data.repo.comment.CommentRepository
import com.example.nikestore.data.repo.comment.CommentRepositoryImpl
import com.example.nikestore.data.repo.product.ProductRepository
import com.example.nikestore.data.repo.product.ProductRepositoryImpl
import com.example.nikestore.data.repo.user.UserRepository
import com.example.nikestore.data.repo.user.UserRepositoryImpl
import com.example.nikestore.data.repo.order.OrderRemoteDataSource
import com.example.nikestore.data.repo.order.OrderRepository
import com.example.nikestore.data.repo.order.OrderRepositoryImpl
import com.example.nikestore.data.repo.banner.BannerRemoteDataSource
import com.example.nikestore.data.repo.cart.CartRemoteDataSource
import com.example.nikestore.data.repo.comment.CommentRemoteDataSource
import com.example.nikestore.data.repo.product.ProductRemoteDataSource
import com.example.nikestore.data.repo.user.UserLocalDataSource
import com.example.nikestore.data.repo.user.UserRemoteDataSource
import com.example.nikestore.feature.ProductDetailViewModel
import com.example.nikestore.feature.auth.AuthViewModel
import com.example.nikestore.feature.cart.CartViewModel
import com.example.nikestore.feature.checkout.CheckoutViewModel
import com.example.nikestore.feature.favorite.FavoriteProductsViewModel
import com.example.nikestore.feature.list.ProductListViewModel
import com.example.nikestore.feature.main.MainActivityViewModel
import com.example.nikestore.feature.main.MainViewModel
import com.example.nikestore.feature.main.ProductListAdapter
import com.example.nikestore.feature.order.OrderHistoryViewModel
import com.example.nikestore.feature.product.comments.CommentsListViewModel
import com.example.nikestore.feature.profile.ProfileViewModel
import com.example.nikestore.feature.shipping.ShippingViewModel
import com.example.nikestore.services.FrescoImageLoadingService
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.services.http.createApiServiceInstance
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        Fresco.initialize(this)

        val myModules = module {
            single { createApiServiceInstance() }
            single<ImageLoadingService> { FrescoImageLoadingService() }
            single { Room.databaseBuilder(this@App,AppDataBase::class.java,"db_app").build() }
            factory<ProductRepository> {
                ProductRepositoryImpl(
                    ProductRemoteDataSource(get()),
                    get<AppDataBase>().productDao()
                )
            }

            single<SharedPreferences> { this@App.getSharedPreferences("app_settings", MODE_PRIVATE) }

            single { UserLocalDataSource(get()) }
            single<UserRepository> { UserRepositoryImpl(
                UserLocalDataSource(get()),
                UserRemoteDataSource(get())
            ) }

            single<OrderRepository> { OrderRepositoryImpl(OrderRemoteDataSource(get())) }
            factory<CartRepository> { CartRepositoryImpl(CartRemoteDataSource(get())) }
            factory { (viewType: Int) -> ProductListAdapter(viewType,get()) }
            factory<BannerRepository> { BannerRepositoryImpl(BannerRemoteDataSource(get())) }
            factory<CommentRepository> { CommentRepositoryImpl(CommentRemoteDataSource(get())) }
            viewModel { MainViewModel(get(), get()) }
            viewModel { (bundle: Bundle) -> ProductDetailViewModel(bundle, get(),get()) }
            viewModel { (productId: Int) -> CommentsListViewModel(productId, get()) }
            viewModel { (sort: Int) -> ProductListViewModel(sort, get()) }
            viewModel{AuthViewModel(get())}
            viewModel{ CartViewModel(get()) }
            viewModel { MainActivityViewModel(get()) }
            viewModel { ShippingViewModel(get()) }
            viewModel { (orderId: Int) -> CheckoutViewModel(orderId, get()) }
            viewModel { ProfileViewModel(get()) }
            viewModel{ FavoriteProductsViewModel(get()) }
            viewModel { OrderHistoryViewModel(get()) }
        }

        startKoin {
            androidContext(this@App)
            modules(myModules)
        }

        val userRepository: UserRepository = get()
        userRepository.loadToken()
    }
}
