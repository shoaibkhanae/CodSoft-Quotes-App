package com.example.quotesapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quotesapp.data.db.entities.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_key WHERE quote_id = :id")
    suspend fun getRemoteKeysByQuoteId(id: String): RemoteKeys?

    @Query("DELETE FROM remote_key")
    suspend fun clearRemoteKeys()

    @Query("SELECT created_at FROM remote_key ORDER BY created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?


}