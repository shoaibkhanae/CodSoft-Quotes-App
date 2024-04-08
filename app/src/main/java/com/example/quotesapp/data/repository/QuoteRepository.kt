package com.example.quotesapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.quotesapp.data.api.QuoteApiService
import com.example.quotesapp.data.db.Quote
import com.example.quotesapp.data.db.QuoteDao
import com.example.quotesapp.data.paging.QuotePagingSource

class QuoteRepository(
    private val service: QuoteApiService,
    private val quoteDao: QuoteDao
) {

    val allQuotes = quoteDao.getAllQuotes()

    fun getQuotes() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { QuotePagingSource(service) }
    ).liveData

    suspend fun insert(quote: Quote) {
        quoteDao.insert(quote)
    }

    suspend fun delete(quote: Quote) {
        quoteDao.delete(quote)
    }
}