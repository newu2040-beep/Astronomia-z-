package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ArticleEntity
import com.example.ui.AstronomyViewModel
import com.example.ui.theme.*

data class PlanetInfo(
    val name: String,
    val color: Color,
    val type: String,
    val distance: String,
    val diameter: String,
    val orbitalPeriod: String,
    val moons: Int,
    val description: String,
    val visibility: String
)

val planetsList = listOf(
    PlanetInfo(
        name = "Jupiter",
        color = PlanetJupiterYellow,
        type = "Gas Giant",
        distance = "628.7 Million km",
        diameter = "139,820 km",
        orbitalPeriod = "11.86 Earth Years",
        moons = 95,
        description = "The largest planet in our Solar System, famed for its Great Red Spot and 4 bright Galilean moons (Io, Europa, Ganymede, Callisto) visible with binoculars.",
        visibility = "Bright yellow-white, visible in Eastern sky after 9:00 PM."
    ),
    PlanetInfo(
        name = "Mars",
        color = PlanetMarsOrange,
        type = "Terrestrial Planet",
        distance = "225.0 Million km",
        diameter = "6,779 km",
        orbitalPeriod = "687 Earth Days",
        moons = 2,
        description = "The Red Planet gets its iconic crimson hue from iron oxide (rust) on its surface. Features Olympus Mons, the largest volcano in the Solar System.",
        visibility = "Distinct reddish hue in Taurus constellation."
    ),
    PlanetInfo(
        name = "Saturn",
        color = PlanetSaturnCyan,
        type = "Gas Giant",
        distance = "1.4 Billion km",
        diameter = "116,460 km",
        orbitalPeriod = "29.45 Earth Years",
        moons = 146,
        description = "The jewel of the solar system, renowned for its spectacular ring system composed of billions of ice chunks and cosmic dust particles.",
        visibility = "Visible in Aquarius constellation late night."
    ),
    PlanetInfo(
        name = "Venus",
        color = Color(0xFFFCD34D),
        type = "Terrestrial Planet",
        distance = "108.2 Million km",
        diameter = "12,104 km",
        orbitalPeriod = "225 Earth Days",
        moons = 0,
        description = "The Morning Star and Evening Star, Venus is wrapped in thick clouds of sulfuric acid, making it the brightest planet in Earth's sky.",
        visibility = "Dazzling beacon in Western sky shortly after sunset."
    )
)

@Composable
fun HomeScreen(
    viewModel: AstronomyViewModel,
    onNavigateToSkyMap: () -> Unit = {}
) {
    var selectedPlanet by remember { mutableStateOf<PlanetInfo?>(null) }
    var selectedSite by remember { mutableStateOf("Palo Alto, CA") }
    var showSitePicker by remember { mutableStateOf(false) }
    var showMoonDetail by remember { mutableStateOf(false) }
    var showFactModal by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCosmicDark),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            ObservationHeader(
                currentSite = selectedSite,
                onClickSite = { showSitePicker = true }
            )
        }
        item {
            BoldImpactHeadline()
        }
        item {
            MoonPhaseHeroCard(onClick = { showMoonDetail = true })
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VISIBLE TONIGHT",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "View Sky Map →",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurpleLight,
                    modifier = Modifier.clickable { onNavigateToSkyMap() }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            VisiblePlanetsRow(onPlanetClick = { planet -> selectedPlanet = planet })
        }
        item {
            SectionTitle("TODAY'S HIGHLIGHTS")
            HighlightsList()
        }
        item {
            SectionTitle("ASTRONOMY FACT")
            AstronomyFactCard(onClick = { showFactModal = true })
        }
    }

    // Planet Detail Dialog
    if (selectedPlanet != null) {
        PlanetDetailDialog(
            planet = selectedPlanet!!,
            onDismiss = { selectedPlanet = null }
        )
    }

    // Site Picker Dialog
    if (showSitePicker) {
        ObservationSitePicker(
            currentSite = selectedSite,
            onSiteSelected = {
                selectedSite = it
                showSitePicker = false
            },
            onDismiss = { showSitePicker = false }
        )
    }

    // Moon Detail Dialog
    if (showMoonDetail) {
        MoonPhaseInspectorDialog(onDismiss = { showMoonDetail = false })
    }

    // Fact Modal
    if (showFactModal) {
        AstronomyFactDialog(onDismiss = { showFactModal = false })
    }
}

@Composable
fun ObservationHeader(
    currentSite: String,
    onClickSite: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onClickSite)
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(SecondaryIndigo, TertiaryPink)
                        )
                    )
                    .padding(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(DeepCosmicDark),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(PrimaryPurpleLight.copy(alpha = 0.6f))
                    )
                }
            }
            Column {
                Text(
                    text = "OBSERVATION SITE",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currentSite,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select Location",
                        tint = PrimaryPurpleLight,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BoldImpactHeadline() {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "CLEAR",
            style = MaterialTheme.typography.displayLarge,
            color = Color.White
        )
        Text(
            text = "NIGHT SKIES",
            style = MaterialTheme.typography.displayLarge,
            color = PrimaryPurpleLight
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Perfect visibility for deep-sky observation tonight.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun MoonPhaseHeroCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(1.dp, CardGlassBorder, RoundedCornerShape(32.dp)),
        colors = CardDefaults.cardColors(containerColor = CardGlassBackground),
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "88",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = 56.sp,
                                fontWeight = FontWeight.Light
                            ),
                            color = Color.White
                        )
                        Text(
                            text = "%",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Text(
                        text = "WANING GIBBOUS",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryPurpleLight,
                        fontWeight = FontWeight.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFFE2E8F0), Color(0xFF64748B), Color(0xFF1E293B))
                            )
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.35f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GlanceCard(
                    title = "MOONRISE",
                    value = "08:12 PM",
                    modifier = Modifier.weight(1f)
                )
                GlanceCard(
                    title = "MOONSET",
                    value = "09:44 AM",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun GlanceCard(title: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Black,
        color = Color.White
    )
}

@Composable
fun VisiblePlanetsRow(onPlanetClick: (PlanetInfo) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        planetsList.take(3).forEach { planet ->
            PlanetPill(
                planet = planet,
                modifier = Modifier.weight(1f),
                onClick = { onPlanetClick(planet) }
            )
        }
    }
}

@Composable
fun PlanetPill(planet: PlanetInfo, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(planet.color.copy(alpha = 0.2f))
                    .border(1.dp, planet.color.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(planet.color)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = planet.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun HighlightsList() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HighlightItem("Eta Aquariids Meteor Shower", "May 5 - May 6, 2025")
        HighlightItem("Moon near Spica", "May 24, 2025 - 9:30 PM")
    }
}

@Composable
fun HighlightItem(title: String, date: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = PrimaryPurpleLight,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun AstronomyFactCard(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "A day on Venus is longer than its year.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Fact",
                tint = PlanetJupiterYellow,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun PlanetDetailDialog(planet: PlanetInfo, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(planet.color)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = planet.name.uppercase(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Color.White)
                    Text(text = planet.type, style = MaterialTheme.typography.labelSmall, color = PrimaryPurpleLight)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = planet.description, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                PlanetStatRow("Distance from Earth", planet.distance)
                PlanetStatRow("Equatorial Diameter", planet.diameter)
                PlanetStatRow("Orbital Period", planet.orbitalPeriod)
                PlanetStatRow("Known Moons", "${planet.moons} moons")

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryPurple.copy(alpha = 0.2f))
                        .padding(12.dp)
                ) {
                    Column {
                        Text("VISIBILITY TONIGHT", style = MaterialTheme.typography.labelSmall, color = PrimaryPurpleLight, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(planet.visibility, style = MaterialTheme.typography.bodySmall, color = Color.White)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun PlanetStatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun ObservationSitePicker(
    currentSite: String,
    onSiteSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sites = listOf("Palo Alto, CA", "Mauna Kea, Hawaii", "Atacama Desert, Chile", "Griffith Observatory, LA", "Lick Observatory, CA")
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = {
            Text("SELECT OBSERVATION SITE", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                sites.forEach { site ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (site == currentSite) PrimaryPurple.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.05f))
                            .clickable { onSiteSelected(site) }
                            .padding(12.dp)
                    ) {
                        Text(site, color = Color.White, fontWeight = if (site == currentSite) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = PrimaryPurpleLight)
            }
        }
    )
}

@Composable
fun MoonPhaseInspectorDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = { Text("MOON PHASE DETAILS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Phase: Waning Gibbous", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Illumination: 88%", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold)
                Text("Moon Age: 18.4 Days", color = Color.White.copy(alpha = 0.7f))
                Text("Moon Distance: 384,400 km", color = Color.White.copy(alpha = 0.7f))
                Text("Next New Moon: June 11", color = Color.White.copy(alpha = 0.7f))
                Text("Next Full Moon: June 25", color = Color.White.copy(alpha = 0.7f))
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close", color = PrimaryPurpleLight) }
        }
    )
}

@Composable
fun AstronomyFactDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = { Text("COSMIC TRIVIA", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("A day on Venus is longer than its year!", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Venus rotates so slowly on its axis that it takes 243 Earth days to complete one rotation, but only 225 Earth days to orbit the Sun once. Furthermore, Venus rotates backwards (retrograde rotation) compared to most other planets!", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Awesome!", color = PrimaryPurpleLight) }
        }
    )
}


