package com.example.quotesapp.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.quotesapp.data.db.entities.Write
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorQuotesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(write: Write)

    @Update
    suspend fun update(write: Write)

    @Delete
    suspend fun delete(write: Write)


    @Query("SELECT * FROM authors")
    fun getAllAuthorsQuotes(): Flow<List<Write>>
}