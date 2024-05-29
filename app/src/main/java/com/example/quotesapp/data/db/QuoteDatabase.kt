package com.example.quotesapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quotesapp.data.db.dao.AuthorQuotesDao
import com.example.quotesapp.data.db.dao.QuoteDao
import com.example.quotesapp.data.db.dao.QuotesDao
import com.example.quotesapp.data.db.dao.RemoteKeysDao
import com.example.quotesapp.data.model.entities.Quote
import com.example.quotesapp.data.model.entities.RemoteKeys
import com.example.quotesapp.data.model.Result
import com.example.quotesapp.data.model.entities.Write

@Database(entities = [Quote::class,Result::class, RemoteKeys::class, Write::class], version = 5, exportSchema = false)
abstract class QuoteDatabase: RoomDatabase() {

    abstract fun getDao(): QuoteDao
    abstract fun getQuotesDao(): QuotesDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
    abstract fun getAuthorQuotesDao(): AuthorQuotesDao
}