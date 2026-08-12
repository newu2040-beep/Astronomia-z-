package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.ArticleEntity
import com.example.ui.AstronomyViewModel
import com.example.ui.theme.*

@Composable
fun SavedScreen(viewModel: AstronomyViewModel) {
    val savedArticles by viewModel.savedArticles.collectAsState()
    var selectedArticle by remember { mutableStateOf<ArticleEntity?>(null) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCosmicDark)
    ) {
        if (savedArticles.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "NO SAVED ARTICLES YET",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap the bookmark icon on any article to save it for offline reading.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(savedArticles) { article ->
                    RecentlyReadItem(
                        article = article,
                        onClick = { selectedArticle = article }
                    )
                }
            }
        }
    }

    if (selectedArticle != null) {
        ArticleReaderDialog(
            article = selectedArticle!!,
            onToggleSaved = { viewModel.toggleSaved(selectedArticle!!.id, selectedArticle!!.isSaved) },
            onDismiss = { selectedArticle = null }
        )
    }
}


