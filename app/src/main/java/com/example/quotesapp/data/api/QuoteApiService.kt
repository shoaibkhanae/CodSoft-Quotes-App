package com.example.quotesapp.data.api

import com.example.quotesapp.data.model.QuoteList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteApiService {
    @GET("/quotes")
    suspend fun getQuotes(@Query("page") page: Int): QuoteList

    @GET("/search/quotes")
    suspend fun searchQuotes(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): Response<QuoteList>

}