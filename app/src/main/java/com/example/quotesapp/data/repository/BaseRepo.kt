package com.example.quotesapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

open class BaseRepo {

    suspend fun <T> safeApiCalls(apiToBeCalled: suspend () -> Response<T>) : Resource<T> {

        return withContext(Dispatchers.IO) {
            try {
                val response: Response<T> = apiToBeCalled()

                if (response.isSuccessful) {
                    Resource.Success(data = response.body()!!)
                } else {
                    Resource.Error(message = "Something went wrong.")
                }
            } catch (e: HttpException) {
                Resource.Error(e.message ?: "Something went wrong")
            } catch (e: IOException) {
                Resource.Error("Please check your internet connection")
            } catch (e: Exception) {
                Resource.Error(message = "Something went wrong")
            }
        }
    }
}