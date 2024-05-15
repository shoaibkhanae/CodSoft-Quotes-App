package com.example.quotesapp.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quotesapp.data.model.Result

@Dao
interface QuotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quotes: List<Result>)

    @Query("SELECT * FROM  quotes ORDER BY page")
    fun getQuotes(): PagingSource<Int, Result>

    @Query("DELETE FROM quotes")
    suspend fun clearAllQuotes()
}