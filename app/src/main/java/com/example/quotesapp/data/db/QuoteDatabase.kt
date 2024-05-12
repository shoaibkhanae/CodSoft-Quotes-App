package com.example.quotesapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Quote::class], version = 3, exportSchema = false)
abstract class QuoteDatabase: RoomDatabase() {

    abstract fun getDao(): QuoteDao
}