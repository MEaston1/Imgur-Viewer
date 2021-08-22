# Imgur-Viewer
Imgur Viewer is a small image sharing application  to view images from Imgur through the ImgurAPI.

## Features
- MVVM Architecture
- Pagination
- A local database
- recyclerview search function
- progress bar
- Separate lists for popular, searched, and favourite images

## Libaries used
- Android Jetpack
  - Viewmodel for storing and managing UI-related data.
  - LiveData To update component observers in an active state.
  - Navigation to utilise Android Jetpack's navigation components for effective navigation.
- Glide for image loading and caching library
- Retrofit2 a Type-safe HTTP client for Android,
- OkHttp3 for logging HTTP requests and responses.
- Material Design for building Material Components for Android.
- Truth for making test assertations and failure messages more readable.
 
 ## Testing
 Local unit testing carried out with JUnit4 and the Truth library, limited to the Room database

## To-Do
- Better understand and fix data models for ImgurAPI
