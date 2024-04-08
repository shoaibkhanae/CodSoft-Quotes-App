package com.example.quotesapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    val author: String
)