package com.example.quotesapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.quotesapp.data.db.Quote
import com.example.quotesapp.data.repository.QuoteRepository
import kotlinx.coroutines.launch

class QuoteViewModel(private val repository: QuoteRepository) : ViewModel() {
    val quotes = repository.getQuotes().cachedIn(viewModelScope)

    val savedQuotes = repository.allQuotes.asLiveData()


    fun insert(quote: Quote) {
        viewModelScope.launch {
            repository.insert(quote)
        }
    }

    fun delete(quote: Quote) {
        viewModelScope.launch {
            repository.delete(quote)
        }
    }
}

