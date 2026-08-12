package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.ArticleEntity
import com.example.ui.AstronomyViewModel
import com.example.ui.theme.*

@Composable
fun ExploreScreen(viewModel: AstronomyViewModel) {
    val articles by viewModel.allArticles.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedArticle by remember { mutableStateOf<ArticleEntity?>(null) }

    val filteredArticles = if (selectedCategory == null) articles else articles.filter { it.category.contains(selectedCategory!!, ignoreCase = true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCosmicDark),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle("CATEGORIES")
                if (selectedCategory != null) {
                    Text(
                        text = "Reset Filter ✕",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurpleLight,
                        modifier = Modifier.clickable { selectedCategory = null }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            CategoriesGrid(
                selectedCategory = selectedCategory,
                onSelectCategory = { cat -> selectedCategory = if (selectedCategory == cat) null else cat }
            )
        }
        
        item {
            SectionTitle("FEATURED ARTICLE")
            Spacer(modifier = Modifier.height(12.dp))
            val featured = filteredArticles.firstOrNull() ?: articles.firstOrNull()
            if (featured != null) {
                FeaturedArticleCard(
                    article = featured, 
                    onClick = { selectedArticle = featured },
                    onToggleSaved = { viewModel.toggleSaved(featured.id, featured.isSaved) }
                )
            }
        }
        
        item {
            SectionTitle("ALL ARTICLES")
            Spacer(modifier = Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                filteredArticles.forEach { article ->
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

@Composable
fun CategoriesGrid(selectedCategory: String?, onSelectCategory: (String) -> Unit) {
    val categories = listOf("Galaxies", "Space Missions", "Black Holes", "Solar System", "Stars", "Exoplanets")
    
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            CategoryCard(categories[0], isSelected = selectedCategory == categories[0], onClick = { onSelectCategory(categories[0]) }, modifier = Modifier.weight(1f))
            CategoryCard(categories[1], isSelected = selectedCategory == categories[1], onClick = { onSelectCategory(categories[1]) }, modifier = Modifier.weight(1f))
            CategoryCard(categories[2], isSelected = selectedCategory == categories[2], onClick = { onSelectCategory(categories[2]) }, modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            CategoryCard(categories[3], isSelected = selectedCategory == categories[3], onClick = { onSelectCategory(categories[3]) }, modifier = Modifier.weight(1f))
            CategoryCard(categories[4], isSelected = selectedCategory == categories[4], onClick = { onSelectCategory(categories[4]) }, modifier = Modifier.weight(1f))
            CategoryCard(categories[5], isSelected = selectedCategory == categories[5], onClick = { onSelectCategory(categories[5]) }, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun CategoryCard(title: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) PrimaryPurple.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.05f))
            .border(1.dp, if (isSelected) PrimaryPurpleLight else Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Filled.Public, contentDescription = null, modifier = Modifier.size(28.dp), tint = if (isSelected) Color.White else PrimaryPurpleLight)
            Spacer(modifier = Modifier.height(6.dp))
            Text(title, style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FeaturedArticleCard(article: ArticleEntity, onClick: () -> Unit, onToggleSaved: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(28.dp))
            .border(1.dp, CardGlassBorder, RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = article.imageUrl,
            contentDescription = article.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(article.title, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(article.summary, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyMedium, maxLines = 2)
        }
        IconButton(
            onClick = onToggleSaved,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
        ) {
            Icon(
                Icons.Filled.Bookmark, 
                contentDescription = "Save", 
                tint = if (article.isSaved) PrimaryPurpleLight else Color.White
            )
        }
    }
}

@Composable
fun RecentlyReadItem(article: ArticleEntity, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(article.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                Text(article.summary, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.6f), maxLines = 1)
            }
        }
    }
}

@Composable
fun ArticleReaderDialog(
    article: ArticleEntity,
    onToggleSaved: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = {
            Column {
                Text(article.category.uppercase(), style = MaterialTheme.typography.labelSmall, color = PrimaryPurpleLight, fontWeight = FontWeight.Black)
                Text(article.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Color.White)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    AsyncImage(
                        model = article.imageUrl,
                        contentDescription = article.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = article.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Astrophysicists and observatory telescopes around the world have unlocked dramatic insights into this cosmic phenomenon. Observations from orbital space stations and ground-based interferometers reveal intricate magnetic fields and particle acceleration mechanics across light-years.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        },
        confirmButton = {
            Row {
                IconButton(onClick = onToggleSaved) {
                    Icon(
                        Icons.Filled.Bookmark,
                        contentDescription = "Save",
                        tint = if (article.isSaved) PrimaryPurpleLight else Color.White
                    )
                }
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "${article.title} - ${article.summary}")
                    }
                    context.startActivity(Intent.createChooser(intent, "Share Article"))
                }) {
                    Icon(Icons.Filled.Share, contentDescription = "Share", tint = Color.White)
                }
                TextButton(onClick = onDismiss) {
                    Text("Close", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}


