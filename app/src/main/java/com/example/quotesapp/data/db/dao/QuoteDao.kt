package com.example.quotesapp.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quotesapp.data.db.entities.Quote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quote: Quote)

    @Delete
    suspend fun delete(quote: Quote)


    @Query("SELECT * FROM saved")
    fun getAllQuotes(): Flow<List<Quote>>
}