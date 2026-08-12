package com.example.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import com.example.ui.theme.*
import kotlin.math.pow
import kotlin.math.sqrt

enum class ToolType {
    MOON_PHASE,
    TELESCOPE,
    DISTANCE,
    STARGAZING_PLANNER,
    COMPASS,
    ANGULAR_SIZE,
    ORBITAL_PERIOD,
    WEIGHT_ON_PLANETS
}

@Composable
fun ToolsScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("ALL", "CALCULATORS", "CONVERTERS", "GUIDES")
    var activeTool by remember { mutableStateOf<ToolType?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCosmicDark)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = DeepCosmicDark,
            contentColor = Color.White,
            edgePadding = 20.dp,
            divider = { HorizontalDivider(color = Color.White.copy(alpha = 0.1f)) },
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = PrimaryPurpleLight
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = if (selectedTab == index) PrimaryPurpleLight else Color.White.copy(alpha = 0.5f)
                        )
                    }
                )
            }
        }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { ToolItem(Icons.Filled.Nightlight, "Moon Phase Calculator", "Calculate exact moon phases & illumination", onClick = { activeTool = ToolType.MOON_PHASE }) }
            item { ToolItem(Icons.Filled.Search, "Telescope Calculator", "Magnification, FOV, focal ratio & aperture", onClick = { activeTool = ToolType.TELESCOPE }) }
            item { ToolItem(Icons.Filled.Sync, "Distance Converter", "km, AU, Light-year & Parsecs converter", onClick = { activeTool = ToolType.DISTANCE }) }
            item { ToolItem(Icons.Filled.WbSunny, "Stargazing Planner", "Optimal observation windows & weather forecast", onClick = { activeTool = ToolType.STARGAZING_PLANNER }) }
            item { ToolItem(Icons.Filled.Explore, "Astronomical Compass", "Real-time celestial cardinal orientation", onClick = { activeTool = ToolType.COMPASS }) }
            item { ToolItem(Icons.Filled.AspectRatio, "Angular Size Calculator", "Determine apparent diameter of celestial objects", onClick = { activeTool = ToolType.ANGULAR_SIZE }) }
            item { ToolItem(Icons.Filled.Speed, "Orbital Period Calculator", "Keplerian motion & gravitational mechanics", onClick = { activeTool = ToolType.ORBITAL_PERIOD }) }
            item { ToolItem(Icons.Filled.Scale, "Weight on Other Planets", "Calculate surface gravity differences across solar system", onClick = { activeTool = ToolType.WEIGHT_ON_PLANETS }) }
        }
    }

    // Interactive Dialogs for Active Tools
    activeTool?.let { tool ->
        when (tool) {
            ToolType.MOON_PHASE -> MoonPhaseDialog(onDismiss = { activeTool = null })
            ToolType.TELESCOPE -> TelescopeDialog(onDismiss = { activeTool = null })
            ToolType.DISTANCE -> DistanceConverterDialog(onDismiss = { activeTool = null })
            ToolType.STARGAZING_PLANNER -> StargazingPlannerDialog(onDismiss = { activeTool = null })
            ToolType.COMPASS -> CompassToolDialog(onDismiss = { activeTool = null })
            ToolType.ANGULAR_SIZE -> AngularSizeDialog(onDismiss = { activeTool = null })
            ToolType.ORBITAL_PERIOD -> OrbitalPeriodDialog(onDismiss = { activeTool = null })
            ToolType.WEIGHT_ON_PLANETS -> WeightOnPlanetsDialog(onDismiss = { activeTool = null })
        }
    }
}

@Composable
fun ToolItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
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
                Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = PrimaryPurpleLight)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.6f))
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = "Go", tint = Color.White.copy(alpha = 0.4f))
        }
    }
}

// 1. Moon Phase Calculator Dialog
@Composable
fun MoonPhaseDialog(onDismiss: () -> Unit) {
    var daysOffset by remember { mutableFloatStateOf(0f) }
    val phaseAge = (18.4f + daysOffset) % 29.53f
    val illumination = kotlin.math.abs((1f - kotlin.math.abs(phaseAge - 14.76f) / 14.76f) * 100).toInt()
    
    val phaseName = when {
        phaseAge < 1.84f -> "New Moon"
        phaseAge < 7.38f -> "Waxing Crescent"
        phaseAge < 9.22f -> "First Quarter"
        phaseAge < 14.76f -> "Waxing Gibbous"
        phaseAge < 16.60f -> "Full Moon"
        phaseAge < 22.14f -> "Waning Gibbous"
        phaseAge < 23.98f -> "Third Quarter"
        else -> "Waning Crescent"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = { Text("MOON PHASE CALCULATOR", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Adjust days offset to preview future/past moon illumination:", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                
                Slider(
                    value = daysOffset,
                    onValueChange = { daysOffset = it },
                    valueRange = -15f..15f,
                    colors = SliderDefaults.colors(thumbColor = PrimaryPurpleLight, activeTrackColor = PrimaryPurple)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Day Offset: ${if (daysOffset >= 0) "+%.0f".format(daysOffset) else "%.0f".format(daysOffset)} days", color = Color.White)
                    Text("Illumination: $illumination%", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(PrimaryPurple.copy(alpha = 0.2f))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(phaseName.uppercase(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Moon Age: %.1f days".format(phaseAge), style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold) } }
    )
}

// 2. Telescope Calculator Dialog
@Composable
fun TelescopeDialog(onDismiss: () -> Unit) {
    var telescopeFocalLength by remember { mutableStateOf("1000") }
    var eyepieceFocalLength by remember { mutableStateOf("10") }
    var apertureDiameter by remember { mutableStateOf("200") }

    val f = telescopeFocalLength.toFloatOrNull() ?: 1000f
    val ep = eyepieceFocalLength.toFloatOrNull() ?: 10f
    val ap = apertureDiameter.toFloatOrNull() ?: 200f

    val magnification = if (ep > 0) f / ep else 0f
    val focalRatio = if (ap > 0) f / ap else 0f
    val maxUsefulMagnification = ap * 2f

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = { Text("TELESCOPE CALCULATOR", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = telescopeFocalLength,
                    onValueChange = { telescopeFocalLength = it },
                    label = { Text("Telescope Focal Length (mm)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurpleLight, unfocusedBorderColor = Color.White.copy(alpha = 0.3f), focusedLabelColor = PrimaryPurpleLight, unfocusedLabelColor = Color.White)
                )
                OutlinedTextField(
                    value = eyepieceFocalLength,
                    onValueChange = { eyepieceFocalLength = it },
                    label = { Text("Eyepiece Focal Length (mm)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurpleLight, unfocusedBorderColor = Color.White.copy(alpha = 0.3f), focusedLabelColor = PrimaryPurpleLight, unfocusedLabelColor = Color.White)
                )
                OutlinedTextField(
                    value = apertureDiameter,
                    onValueChange = { apertureDiameter = it },
                    label = { Text("Aperture Diameter (mm)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurpleLight, unfocusedBorderColor = Color.White.copy(alpha = 0.3f), focusedLabelColor = PrimaryPurpleLight, unfocusedLabelColor = Color.White)
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                Text("Magnification: %.0fx".format(magnification), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryPurpleLight)
                Text("Focal Ratio: f/%.1f".format(focalRatio), color = Color.White)
                Text("Max Useful Magnification: %.0fx".format(maxUsefulMagnification), color = Color.White.copy(alpha = 0.7f))
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold) } }
    )
}

// 3. Distance Converter Dialog
@Composable
fun DistanceConverterDialog(onDismiss: () -> Unit) {
    var valueInput by remember { mutableStateOf("1") }
    val ly = valueInput.toDoubleOrNull() ?: 1.0

    val km = ly * 9.461e12
    val au = ly * 63241.1
    val pc = ly * 0.306601

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = { Text("ASTRONOMICAL DISTANCE CONVERTER", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = valueInput,
                    onValueChange = { valueInput = it },
                    label = { Text("Value in Light-Years (ly)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurpleLight, unfocusedBorderColor = Color.White.copy(alpha = 0.3f), focusedLabelColor = PrimaryPurpleLight, unfocusedLabelColor = Color.White)
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                Text("Kilometers: %.2e km".format(km), fontWeight = FontWeight.Bold, color = PrimaryPurpleLight)
                Text("Astronomical Units (AU): %.2f AU".format(au), color = Color.White)
                Text("Parsecs: %.4f pc".format(pc), color = Color.White)
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold) } }
    )
}

// 4. Stargazing Planner Dialog
@Composable
fun StargazingPlannerDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = { Text("STARGAZING PLANNER", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Observation Forecast (Tonight):", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Text("• Cloud Cover: 5% (Excellent)", color = Color(0xFF4ADE80))
                Text("• Atmospheric Seeing: Index 4/5 (Minimal Turbulence)", color = PrimaryPurpleLight)
                Text("• Transparency: High (Bortle Class 3)", color = Color.White)
                Text("• Best Observation Hour: 10:30 PM - 3:00 AM", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Got it", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold) } }
    )
}

// 5. Compass Tool Dialog
@Composable
fun CompassToolDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = { Text("ASTRONOMICAL COMPASS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(3.dp, PrimaryPurpleLight, CircleShape)
                        .background(Color.White.copy(alpha = 0.05f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("312° NW", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Polaris Target", style = MaterialTheme.typography.labelSmall, color = PrimaryPurpleLight)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Azimuth: 312.4° • Altitude: 37.4°", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold) } }
    )
}

// 6. Angular Size Dialog
@Composable
fun AngularSizeDialog(onDismiss: () -> Unit) {
    var diameterKm by remember { mutableStateOf("3474") } // Moon diameter
    var distanceKm by remember { mutableStateOf("384400") } // Moon distance

    val d = diameterKm.toDoubleOrNull() ?: 3474.0
    val dist = distanceKm.toDoubleOrNull() ?: 384400.0
    val arcMin = (2.0 * kotlin.math.atan(d / (2.0 * dist)) * (180.0 / Math.PI) * 60.0)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = { Text("ANGULAR SIZE CALCULATOR", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = diameterKm,
                    onValueChange = { diameterKm = it },
                    label = { Text("Object Diameter (km)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurpleLight, unfocusedBorderColor = Color.White.copy(alpha = 0.3f), focusedLabelColor = PrimaryPurpleLight, unfocusedLabelColor = Color.White)
                )
                OutlinedTextField(
                    value = distanceKm,
                    onValueChange = { distanceKm = it },
                    label = { Text("Distance to Object (km)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurpleLight, unfocusedBorderColor = Color.White.copy(alpha = 0.3f), focusedLabelColor = PrimaryPurpleLight, unfocusedLabelColor = Color.White)
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                Text("Apparent Angular Diameter:", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                Text("%.2f arcminutes (%.0f arcseconds)".format(arcMin, arcMin * 60.0), fontWeight = FontWeight.Bold, color = PrimaryPurpleLight)
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold) } }
    )
}

// 7. Orbital Period Dialog
@Composable
fun OrbitalPeriodDialog(onDismiss: () -> Unit) {
    var semimajorAU by remember { mutableStateOf("1.524") } // Mars AU
    val a = semimajorAU.toDoubleOrNull() ?: 1.524
    val years = sqrt(a.pow(3.0))

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = { Text("ORBITAL PERIOD CALCULATOR", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Kepler's 3rd Law: T² = a³", style = MaterialTheme.typography.labelSmall, color = PrimaryPurpleLight, fontWeight = FontWeight.Black)
                OutlinedTextField(
                    value = semimajorAU,
                    onValueChange = { semimajorAU = it },
                    label = { Text("Semi-Major Axis (AU)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurpleLight, unfocusedBorderColor = Color.White.copy(alpha = 0.3f), focusedLabelColor = PrimaryPurpleLight, unfocusedLabelColor = Color.White)
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                Text("Calculated Orbital Period:", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                Text("%.2f Earth Years (%.0f days)".format(years, years * 365.25), fontWeight = FontWeight.Bold, color = PrimaryPurpleLight)
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold) } }
    )
}

// 8. Weight on Other Planets Dialog
@Composable
fun WeightOnPlanetsDialog(onDismiss: () -> Unit) {
    var weightInput by remember { mutableStateOf("70") }
    val w = weightInput.toDoubleOrNull() ?: 70.0

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = { Text("WEIGHT ON OTHER PLANETS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text("Your Weight on Earth (kg or lbs)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurpleLight, unfocusedBorderColor = Color.White.copy(alpha = 0.3f), focusedLabelColor = PrimaryPurpleLight, unfocusedLabelColor = Color.White)
                )

                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("• Mercury (38%): %.1f".format(w * 0.38), color = Color.White)
                    Text("• Venus (91%): %.1f".format(w * 0.91), color = Color.White)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("• Mars (38%): %.1f".format(w * 0.38), color = PlanetMarsOrange, fontWeight = FontWeight.Bold)
                    Text("• Jupiter (234%): %.1f".format(w * 2.34), color = PlanetJupiterYellow, fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("• Saturn (106%): %.1f".format(w * 1.06), color = PlanetSaturnCyan, fontWeight = FontWeight.Bold)
                    Text("• Moon (16.6%): %.1f".format(w * 0.166), color = PrimaryPurpleLight, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold) } }
    )
}


