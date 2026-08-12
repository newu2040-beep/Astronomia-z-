package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY timestamp DESC")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE isSaved = 1 ORDER BY timestamp DESC")
    fun getSavedArticles(): Flow<List<ArticleEntity>>
    
    @Query("SELECT * FROM articles WHERE isReadLater = 1 ORDER BY timestamp DESC")
    fun getReadLaterArticles(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("UPDATE articles SET isSaved = :isSaved WHERE id = :id")
    suspend fun updateSavedStatus(id: String, isSaved: Boolean)
    
    @Query("UPDATE articles SET isReadLater = :isReadLater WHERE id = :id")
    suspend fun updateReadLaterStatus(id: String, isReadLater: Boolean)

    @Query("DELETE FROM articles WHERE id = :id")
    suspend fun deleteArticleById(id: String)
}
