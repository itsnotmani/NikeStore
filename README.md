

## Overview

An Android application that is my first large and comprehensive project developed with kotlin , a shoe store app named **NikeStore**. This was the final project of the Android course from the 7Learn website, featuring elements like banners, a shopping cart, user accounts, and other features, developed following the MVVM (Model-View-ViewModel) architecture.

## Download

**Note:** The app's registration section is currently experiencing issues due to a server problem. You can use the following username and password to access this section:


**Username: tesss@gmail.com**


**Password: 123456**


[Download NikeStore](https://biaupload.com/do.php?filename=org-b923a669363b1.apk)



## Features

- **Slider**: Displays promotional images and special offers. The slider allows users to stay informed about the latest and most popular products and discounts and to easily navigate through them.
 
- **Banner**: Highlights special discounts and new products. Banners are prominently displayed on the homepage to encourage users to purchase specific discounted products.
 
- **Bottom Navigation**: Provides easy navigation between different sections of the app. This navigation helps users easily switch between sections such as Home, Categories, Cart, and Profile.
 
- **Shopping Cart**: Allows adding, removing, and viewing items before purchase and calculating the prices of products in the cart. Users can add their desired products to the cart, change the quantity, and view the final price.
 
- **Online Purchase**: Enables online purchasing and using a demo payment gateway for utilizing this feature and receiving a receipt after purchase. This feature allows users to buy products online and make secure payments through payment gateways.
 
- **User Account**: Allows users to create and manage their profiles. Users can create their accounts and manage their personal information and orders.
 
- **Product List**: Displays various shoes with details such as price, size, and description, and allows filtering products based on price and other criteria. This feature helps users easily find their desired products.
 
- **Order History**: Displays past orders. Users can view their order history and check the details of each order.
 
- **Wishlist**: Allows saving favorite items for future purchases using the Room library. Users can add their favorite products to the wishlist for easy access in the future.
 
- **Comments Display**: Displays comments left about products. Users can view other users' opinions about products and make better purchasing decisions.

---

## Technologies and Libraries Used

### AndroidX Libraries

- **Navigation**: Used `navigation-runtime-ktx` and `navigation-fragment-ktx` for managing navigation between screens.
- **DataBinding**: Utilized `databinding-runtime` to directly bind Java/Kotlin code to UI components.
- **Core KTX**: Provides additional features and makes coding easier for developers.
- **AppCompat**: Supports modern Android features on older versions.
- **Material Components**: Implements material design components to enhance user experience.
- **ConstraintLayout**: Allows flexible and responsive UI design.
- **Room**: Manages SQLite database with `room-common`, `room-runtime`, and `room-rxjava2`.

### Dependency Injection

- **Koin**: Uses `koin-android` for dependency injection and easier management.

### Reactive Programming

- **RxJava**: Implements `rxandroid` and `rxjava` for reactive programming and handling asynchronous operations.

### Network and Serialization

- **Retrofit**: Utilizes `retrofit`, `adapter-rxjava2`, and `converter-gson` for API communication and data serialization/deserialization.
- **Gson**: Converts Java objects to JSON and vice versa.

### Image Loading

- **Fresco**: A library for managing images with advanced image loading capabilities.

### Logging

- **Timber**: For managing logs and better debugging.

### Event Handling

- **EventBus**: Manages events and communication between different parts of the app.

### Additional Libraries

- **ViewPager2**: For displaying pages in a pageable manner.
- **Dynamic Animation**: For creating dynamic and smooth animations.
- **OkHttp Logging Interceptor**: For logging network requests and responses.
- **Browser**: For handling internal browser interactions.

### Testing

- **JUnit**: For unit testing.
- **Espresso**: For Android UI testing.


These libraries and technologies generally enhance performance, ease development, and increase the capabilities of the NikeStore app.

