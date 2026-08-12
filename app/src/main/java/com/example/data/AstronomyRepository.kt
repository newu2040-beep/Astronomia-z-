package com.example.data

import kotlinx.coroutines.flow.Flow

class AstronomyRepository(private val articleDao: ArticleDao) {
    val allArticles: Flow<List<ArticleEntity>> = articleDao.getAllArticles()
    val savedArticles: Flow<List<ArticleEntity>> = articleDao.getSavedArticles()
    val readLaterArticles: Flow<List<ArticleEntity>> = articleDao.getReadLaterArticles()

    suspend fun insert(article: ArticleEntity) = articleDao.insertArticle(article)
    
    suspend fun insertAll(articles: List<ArticleEntity>) = articleDao.insertArticles(articles)

    suspend fun toggleSaved(id: String, isSaved: Boolean) = articleDao.updateSavedStatus(id, isSaved)
    
    suspend fun toggleReadLater(id: String, isReadLater: Boolean) = articleDao.updateReadLaterStatus(id, isReadLater)
    
    // Seed some initial data if empty
    suspend fun seedDataIfNeeded() {
        // Just a simple check, ideally we'd query count
        // We'll populate this from the ViewModel for now
    }
}
