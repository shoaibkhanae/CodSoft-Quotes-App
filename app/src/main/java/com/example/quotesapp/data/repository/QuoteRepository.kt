package com.example.quotesapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.quotesapp.data.api.QuoteApiService
import com.example.quotesapp.data.db.dao.QuoteDao
import com.example.quotesapp.data.db.QuoteDatabase
import com.example.quotesapp.data.model.entities.Quote
import com.example.quotesapp.data.model.QuoteList
import com.example.quotesapp.data.paging.remotemediator.QuoteRemoteMediator
import com.example.quotesapp.utils.Response
import javax.inject.Inject

class QuoteRepository @Inject constructor(
    private val service: QuoteApiService,
    private val database: QuoteDatabase,
    private val quoteDao: QuoteDao
) {

    val allQuotes = quoteDao.getAllQuotes()

    private val _searched = MutableLiveData<Response<QuoteList>>()
    val searched: LiveData<Response<QuoteList>> = _searched

    @OptIn(ExperimentalPagingApi::class)
    fun getQuotes() = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100
        ),
        pagingSourceFactory = {
            database.getQuotesDao().getQuotes()
        },
        remoteMediator = QuoteRemoteMediator(
            service,
            database
        )
    ).liveData

    suspend fun searchQuotes(search: String) {
        try {
            _searched.postValue(Response.Loading())

            val result = service.searchQuotes(search)

            if (result.body() != null) {
                _searched.postValue(Response.Success(result.body()))
            }

        } catch (e: Exception) {
            _searched.postValue(Response.Error(e.message.toString()))
        }
    }

    suspend fun insert(quote: Quote) {
        quoteDao.insert(quote)
    }

    suspend fun delete(quote: Quote) {
        quoteDao.delete(quote)
    }
}