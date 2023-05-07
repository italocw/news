
# News
[![Kotlin Version](https://img.shields.io/badge/kotlin-1.8.2-blue.svg)](https://kotlinlang.org) [![Min Sdk](https://img.shields.io/badge/minSdk-24-green.svg)](https://developer.android.com/about/versions/android-7.0)

An Android app for reading news articles from various sources.

## Features

-   Display news articles from various sources using the News API
-   Search for news articles by keyword
-   View some details of each news article, including a link to the original article
-   Swipe to refresh news articles, similar to the Gmail Android app

### Coming Soon

#### Save Favorite News Articles

The Room database implementation is already functional and will be used to implement a feature that allows users to save favorite news articles for offline access. This feature will be added in a future update to the app.


## Architecture

This project follows a single-activity architecture, which means that all the UI components of the app are contained within a single activity. Navigation between different screens of the app is managed by the Navigation component of Jetpack.


## Libraries
Some of the libraries used by this app project are listed above:

### Android Jetpack
The app incorporates various features and libraries from the [Android Jetpack](https://developer.android.com/jetpack), such as the [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started), [DataBinding](https://developer.android.com/topic/libraries/data-binding), [ LiveData](https://developer.android.com/topic/libraries/architecture/livedata), and Coroutines, to enhance the development process. It also leverages  [Jetpack Compose](https://developer.android.com/jetpack/compose) for part of its UI implementation, while the rest is written using XML files.	 [Room](https://developer.android.com/training/data-storage/room)  database-releated classes are already implemented to be used on the comming soon feature to save articles offline.

### Other libraries
-   [Koin](https://insert-koin.io/) for dependency injection.
-   [Retrofit](https://square.github.io/retrofit/) for making HTTP requests to the news API.
-   [Coil](https://coil-kt.github.io/coil/) for image loading and caching.



## Running the project

1.  Clone the repository
2.  Select the main branch
3.  Add your own Google News API key to the local.properties file:
    `api_key=YOUR_API_KEY_HERE`.
    Make sure to replace `YOUR_API_KEY_HERE` with your own API key.
4. Import in it on Android Studio
5. Build and run the app.
