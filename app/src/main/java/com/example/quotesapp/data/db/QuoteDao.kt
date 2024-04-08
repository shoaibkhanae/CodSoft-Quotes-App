package com.example.quotesapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(quote: Quote)

    @Delete
    suspend fun delete(quote: Quote)


    @Query("SELECT * FROM quotes")
    fun getAllQuotes(): Flow<List<Quote>>
}