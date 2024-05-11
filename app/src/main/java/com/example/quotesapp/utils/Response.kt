package com.example.quotesapp.utils

sealed class Response<T>(val data: T? = null, val error: String? = null) {
    class Loading<T>: Response<T>()
    class Success<T>(data: T? = null) : Response<T>(data = data)
    class Error<T>(message: String) : Response<T>(error = message)
}
