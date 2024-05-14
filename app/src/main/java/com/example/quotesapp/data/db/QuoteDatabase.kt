package com.example.quotesapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quotesapp.data.model.Quote
import com.example.quotesapp.data.model.RemoteKeys
import com.example.quotesapp.data.model.Result

@Database(entities = [Quote::class,Result::class,RemoteKeys::class], version = 4, exportSchema = false)
abstract class QuoteDatabase: RoomDatabase() {

    abstract fun getDao(): QuoteDao
    abstract fun getQuotesDao(): QuotesDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}