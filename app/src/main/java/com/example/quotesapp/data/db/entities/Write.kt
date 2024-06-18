package com.example.quotesapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authors")
data class Write(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("Content")
    val content: String,
    @ColumnInfo("Author")
    val author: String
)
