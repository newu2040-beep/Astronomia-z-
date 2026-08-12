package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val category: String,
    val imageUrl: String,
    val isSaved: Boolean = false,
    val isReadLater: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
