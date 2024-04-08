package com.example.quotesapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quotesapp.data.repository.QuoteRepository

class QuoteViewModelFactory(private val repository: QuoteRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuoteViewModel::class.java)) {
            return QuoteViewModel(repository) as T
        }
        throw IllegalArgumentException("unknown viewModel class")
    }
}