package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var showDialogTitle by remember { mutableStateOf<String?>(null) }
    var showDialogContent by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCosmicDark)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ABOUT ASTRONOMYZ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = Color.White
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // App Hero Crest
            item {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(PrimaryPurpleLight, SecondaryIndigo, DeepCosmicDark)
                            )
                        )
                        .border(2.dp, PrimaryPurpleLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AstronomyZ Emblem",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "ASTRONOMYZ",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 2.sp
                )

                Text(
                    text = "Version 2.4.0 (Build 2026.08)",
                    style = MaterialTheme.typography.bodySmall,
                    color = PrimaryPurpleLight,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "The ultimate companion for stargazers, amateur astronomers, and space enthusiasts. Track real-time celestial events, planet alignments, moon phases, and deep space phenomena.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Developer / Team info
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, CardGlassBorder, RoundedCornerShape(24.dp))
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "DESIGN & DEVELOPMENT",
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryPurpleLight,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Crafted with ❤️ by AstronomyZ Team",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Engineered with Jetpack Compose & Kotlin Material 3",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // Interactive Links
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                ) {
                    AboutRowItem(
                        icon = Icons.Default.Security,
                        title = "Privacy Policy",
                        onClick = {
                            showDialogTitle = "Privacy Policy"
                            showDialogContent = "AstronomyZ respects your personal privacy. Location coordinates are strictly processed locally on your device to calculate celestial altitudes, star map alignments, and local moonrise times. No personal data or location histories are ever transmitted or sold to external servers."
                        }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                    AboutRowItem(
                        icon = Icons.Default.Gavel,
                        title = "Terms of Service",
                        onClick = {
                            showDialogTitle = "Terms of Service"
                            showDialogContent = "By using AstronomyZ, you agree to use celestial calculation services responsibly. Stargazing forecasts and meteor shower predictions are generated using NASA astronomical ephemeris models."
                        }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                    AboutRowItem(
                        icon = Icons.Default.Source,
                        title = "Celestial Data & Sources",
                        onClick = {
                            showDialogTitle = "Celestial Data & Sources"
                            showDialogContent = "Data provided in part by:\n• NASA JPL Horizons Ephemeris System\n• ESA Gaia Star Catalog\n• OpenNGC Deep Sky Object Database\n• US Naval Observatory Moon Phase Algorithms"
                        }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                    AboutRowItem(
                        icon = Icons.Default.StarRate,
                        title = "Rate AstronomyZ",
                        onClick = {
                            Toast.makeText(context, "Thank you for rating AstronomyZ 5 stars! ⭐⭐⭐⭐⭐", Toast.LENGTH_LONG).show()
                        }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                    AboutRowItem(
                        icon = Icons.Default.Share,
                        title = "Share with Fellow Stargazers",
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putchar(Intent.EXTRA_TEXT, "Check out AstronomyZ - the ultimate stargazing and sky map app!")
                            }
                            context.startActivity(Intent.createChooser(intent, "Share AstronomyZ"))
                        }
                    )
                }
            }

            item {
                Text(
                    text = "© 2026 AstronomyZ Studio. All rights reserved.",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }

    if (showDialogTitle != null && showDialogContent != null) {
        AlertDialog(
            onDismissRequest = {
                showDialogTitle = null
                showDialogContent = null
            },
            containerColor = SurfaceCosmicDark,
            title = {
                Text(
                    text = showDialogTitle!!,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            },
            text = {
                Text(
                    text = showDialogContent!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialogTitle = null
                        showDialogContent = null
                    }
                ) {
                    Text("Close", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

fun Intent.putchar(key: String, value: String) {
    this.putExtra(key, value)
}

@Composable
fun AboutRowItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryPurpleLight,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.3f),
            modifier = Modifier.size(20.dp)
        )
    }
}
