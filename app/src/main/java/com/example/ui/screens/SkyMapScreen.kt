package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*
import kotlin.random.Random

@Composable
fun SkyMapScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCosmicDark),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        
        // Sky Map Area
        Box(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(SurfaceCosmicDark)
                .border(2.dp, PrimaryPurple.copy(alpha = 0.5f), CircleShape)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                
                // Draw stars
                val random = Random(42)
                val stars = mutableListOf<Offset>()
                for (i in 0..200) {
                    val x = random.nextFloat() * width
                    val y = random.nextFloat() * height
                    stars.add(Offset(x, y))
                    drawCircle(
                        color = Color.White.copy(alpha = random.nextFloat() * 0.8f + 0.2f),
                        radius = random.nextFloat() * 3f,
                        center = Offset(x, y)
                    )
                }
                
                // Draw constellation lines
                if (stars.size > 10) {
                    val points = listOf(stars[0], stars[1], stars[5], stars[10], stars[8])
                    drawPoints(
                        points = points,
                        pointMode = PointMode.Polygon,
                        color = PrimaryPurpleLight.copy(alpha = 0.6f),
                        strokeWidth = 3f
                    )
                }
            }
            
            // Cardinal directions
            Text("N", color = PrimaryPurpleLight, fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.TopCenter).padding(8.dp))
            Text("S", color = PrimaryPurpleLight, fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp))
            Text("E", color = PrimaryPurpleLight, fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.CenterStart).padding(8.dp))
            Text("W", color = PrimaryPurpleLight, fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.CenterEnd).padding(8.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Selected Object Info
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(PlanetJupiterYellow.copy(alpha = 0.2f))
                        .border(1.dp, PlanetJupiterYellow.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(PlanetJupiterYellow)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("JUPITER", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Color.White)
                    Text("Planet • Visible in East", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Column {
                            Text("MAGNITUDE", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.4f))
                            Text("-2.20", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(28.dp))
                        Column {
                            Text("DISTANCE", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.4f))
                            Text("628.7M km", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Bottom Tools
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {}) { Icon(Icons.Filled.Search, contentDescription = "Search", tint = PrimaryPurpleLight) }
            IconButton(onClick = {}) { Icon(Icons.Filled.Nightlight, contentDescription = "Tonight", tint = Color.White) }
            IconButton(onClick = {}) { Icon(Icons.Filled.Explore, contentDescription = "Compass", tint = Color.White) }
            IconButton(onClick = {}) { Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color.White) }
        }
    }
}

