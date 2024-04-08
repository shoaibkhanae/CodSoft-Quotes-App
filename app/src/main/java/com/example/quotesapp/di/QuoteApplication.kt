package com.example.quotesapp.di

import android.app.Application
import com.example.quotesapp.data.api.QuoteApiService
import com.example.quotesapp.data.api.RetrofitHelper
import com.example.quotesapp.data.db.QuoteDatabase
import com.example.quotesapp.data.repository.QuoteRepository

class QuoteApplication: Application() {
    private val database by lazy { QuoteDatabase.getDatabase(this) }
    private val api by lazy { RetrofitHelper.getInstance().create(QuoteApiService::class.java) }
    val repository by lazy { QuoteRepository(api,database.getDao()) }
}
