package com.view.imgurviewer
// a class recommended by google to wrap around network responses, useful for error responses and loading states
sealed class Resource<T>(
        val data: T? = null,
        val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)       // inherits from resource, passes data and a message
    class Loading<T> : Resource<T>()
}