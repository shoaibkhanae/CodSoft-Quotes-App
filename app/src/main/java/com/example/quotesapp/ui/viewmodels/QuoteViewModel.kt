package com.example.quotesapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.quotesapp.data.model.QuoteList
import com.example.quotesapp.data.db.entities.Quote
import com.example.quotesapp.data.db.entities.Write
import com.example.quotesapp.data.repository.QuoteRepository
import com.example.quotesapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(private val repository: QuoteRepository) : ViewModel() {

    val quotes = repository.getQuotes().cachedIn(viewModelScope)
    val searched: LiveData<Response<QuoteList>> = repository.searched
    val savedQuotes = repository.allQuotes.asLiveData()
    val authorsQuotes = repository.allAuthorsQuotes.asLiveData()

    fun insert(quote: Quote) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(quote)
        }
    }

    fun searchQuotes(search: String) {
        viewModelScope.launch {
            repository.searchQuotes(search)
        }
    }

    fun delete(quote: Quote) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(quote)
        }
    }

    fun insert(write: Write) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(write)
        }
    }

    fun update(write: Write) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(write)
        }
    }

    fun delete(write: Write) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(write)
        }
    }
}

