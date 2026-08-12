package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ArticleEntity
import com.example.data.AstronomyRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AstronomyViewModel(private val repository: AstronomyRepository) : ViewModel() {

    val allArticles: StateFlow<List<ArticleEntity>> = repository.allArticles
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val savedArticles: StateFlow<List<ArticleEntity>> = repository.savedArticles
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            seedInitialData()
        }
    }

    private suspend fun seedInitialData() {
        val initialData = listOf(
            ArticleEntity("1", "The Wonders of the Milky Way", "Our home galaxy is more vast and mysterious than we ever imagined.", "Galaxies", "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?q=80&w=2000", isSaved = true),
            ArticleEntity("2", "Hubble Space Telescope", "The telescope that changed our view of the universe.", "Space Missions", "https://images.unsplash.com/photo-1451187580459-43490279c0fa?q=80&w=2000"),
            ArticleEntity("3", "Mars Rover Curiosity", "Exploring the surface of the Red Planet.", "Space Missions", "https://images.unsplash.com/photo-1614730321146-b6fa6a46bcb4?q=80&w=2000"),
            ArticleEntity("4", "Black Holes Explained", "What happens when gravity becomes infinite?", "Black Holes", "https://images.unsplash.com/photo-1464802686167-b939a6910659?q=80&w=2000")
        )
        // Note: For a real app, only seed if empty. Here we just insert/replace on startup to have data.
        repository.insertAll(initialData)
    }

    fun toggleSaved(id: String, currentSaved: Boolean) {
        viewModelScope.launch {
            repository.toggleSaved(id, !currentSaved)
        }
    }
}

class AstronomyViewModelFactory(private val repository: AstronomyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AstronomyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AstronomyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
