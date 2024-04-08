package com.example.quotesapp.data.repository

sealed class Resource<T>(
    val data: T? = null,
    val error: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(message: String) : Resource<T>(error = message)
    class Loading<T> : Resource<T>()
}
