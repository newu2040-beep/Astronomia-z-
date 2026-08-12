package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current

    // State for permissions
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launchers
    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) {
            Toast.makeText(context, "Notification permission granted!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Notification permission denied.", Toast.LENGTH_SHORT).show()
        }
    }

    val locationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        if (isGranted) {
            Toast.makeText(context, "Location access granted! Observing site updated.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Location permission denied.", Toast.LENGTH_SHORT).show()
        }
    }

    // Settings State
    var issAlertsEnabled by remember { mutableStateOf(true) }
    var meteorAlertsEnabled by remember { mutableStateOf(true) }
    var moonAlertsEnabled by remember { mutableStateOf(false) }
    var clearSkyAlertsEnabled by remember { mutableStateOf(true) }
    
    var eyeComfortMode by remember { mutableStateOf(false) }
    var distanceUnit by remember { mutableStateOf("Kilometers (km)") }
    var starMagnitudeLimit by remember { mutableFloatStateOf(6.0f) }
    
    var selectedLocation by remember { mutableStateOf("Palo Alto, CA (37.4419° N, 122.1430° W)") }
    var showLocationDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (eyeComfortMode) Color(0xFF1A0000) else DeepCosmicDark)
    ) {
        // Header
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
                    tint = if (eyeComfortMode) Color(0xFFFF5555) else Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "SETTINGS & PREFERENCES",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = if (eyeComfortMode) Color(0xFFFF5555) else Color.White
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Permissions Section
            item {
                SettingsCategoryTitle("SYSTEM PERMISSIONS", eyeComfortMode)
                Spacer(modifier = Modifier.height(8.dp))
                
                PermissionCard(
                    title = "Push Notifications",
                    description = "Required for stargazing alerts, meteor showers, & satellite passes",
                    icon = Icons.Default.Notifications,
                    isGranted = hasNotificationPermission,
                    onRequestPermission = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            Toast.makeText(context, "Notifications enabled for this device", Toast.LENGTH_SHORT).show()
                        }
                    },
                    eyeComfortMode = eyeComfortMode
                )
                
                Spacer(modifier = Modifier.height(10.dp))

                PermissionCard(
                    title = "Location Access",
                    description = "Used to align real-time Sky Map & precise celestial coordinates",
                    icon = Icons.Default.MyLocation,
                    isGranted = hasLocationPermission,
                    onRequestPermission = {
                        locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    },
                    eyeComfortMode = eyeComfortMode
                )
            }

            // Stargazing Alerts Section
            item {
                SettingsCategoryTitle("STARGAZING ALERTS", eyeComfortMode)
                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SettingToggleRow(
                        title = "ISS & Satellite Flyovers",
                        subtitle = "Alert 15 mins before visible bright passes",
                        checked = issAlertsEnabled,
                        onCheckedChange = { issAlertsEnabled = it },
                        eyeComfortMode = eyeComfortMode
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                    SettingToggleRow(
                        title = "Meteor Shower Peaks",
                        subtitle = "Perseids, Geminids, and major activity peaks",
                        checked = meteorAlertsEnabled,
                        onCheckedChange = { meteorAlertsEnabled = it },
                        eyeComfortMode = eyeComfortMode
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                    SettingToggleRow(
                        title = "Moon Phase Transitions",
                        subtitle = "Full moon, new moon & eclipse warnings",
                        checked = moonAlertsEnabled,
                        onCheckedChange = { moonAlertsEnabled = it },
                        eyeComfortMode = eyeComfortMode
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                    SettingToggleRow(
                        title = "Clear Night Stargazing Windows",
                        subtitle = "Get notified when forecast predicts 90%+ visibility",
                        checked = clearSkyAlertsEnabled,
                        onCheckedChange = { clearSkyAlertsEnabled = it },
                        eyeComfortMode = eyeComfortMode
                    )
                }
            }

            // Display & Stargazing Mode
            item {
                SettingsCategoryTitle("OBSERVATION DISPLAY", eyeComfortMode)
                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SettingToggleRow(
                        title = "Red-Light Night Vision Mode",
                        subtitle = "Preserves night adaptation during dark-sky sessions",
                        checked = eyeComfortMode,
                        onCheckedChange = { eyeComfortMode = it },
                        eyeComfortMode = eyeComfortMode
                    )

                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                    // Magnitude Limit Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Star Magnitude Limit",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (eyeComfortMode) Color(0xFFFF5555) else Color.White
                            )
                            Text(
                                text = "Mag %.1f".format(starMagnitudeLimit),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Black,
                                color = PrimaryPurpleLight
                            )
                        }
                        Text(
                            text = "Higher value shows fainter stars on the Sky Map",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Slider(
                            value = starMagnitudeLimit,
                            onValueChange = { starMagnitudeLimit = it },
                            valueRange = 3.0f..8.0f,
                            steps = 10,
                            colors = SliderDefaults.colors(
                                thumbColor = PrimaryPurpleLight,
                                activeTrackColor = PrimaryPurple
                            )
                        )
                    }
                }
            }

            // Observation Location
            item {
                SettingsCategoryTitle("OBSERVATION LOCATION", eyeComfortMode)
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                        .clickable { showLocationDialog = true }
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PrimaryPurple.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                tint = PrimaryPurpleLight
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Current Site",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = selectedLocation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Change Location",
                            tint = Color.White.copy(alpha = 0.4f)
                        )
                    }
                }
            }

            // Data & Actions
            item {
                SettingsCategoryTitle("CACHE & SYSTEM", eyeComfortMode)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            Toast.makeText(context, "Celestial cache cleared successfully", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceVariantDark,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Clear Cache", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            issAlertsEnabled = true
                            meteorAlertsEnabled = true
                            moonAlertsEnabled = false
                            clearSkyAlertsEnabled = true
                            eyeComfortMode = false
                            starMagnitudeLimit = 6.0f
                            Toast.makeText(context, "Settings reset to defaults", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceVariantDark,
                            contentColor = PrimaryPurpleLight
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reset All", fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // Location Dialog
    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            containerColor = SurfaceCosmicDark,
            titleContentColor = Color.White,
            title = {
                Text(
                    text = "SELECT OBSERVATION SITE",
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val sites = listOf(
                        "Palo Alto, CA (37.4419° N, 122.1430° W)",
                        "Mauna Kea Observatory, Hawaii (19.8207° N, 155.4681° W)",
                        "Atacama Large Millimeter Array, Chile (23.0225° S, 67.7550° W)",
                        "Lick Observatory, Mt. Hamilton, CA (37.3414° N, 121.6429° W)",
                        "Griffith Observatory, Los Angeles, CA (34.1184° N, 118.3004° W)",
                        "Roque de los Muchachos, La Palma (28.7610° N, 17.8817° W)"
                    )
                    sites.forEach { site ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (selectedLocation == site) PrimaryPurple.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.05f))
                                .clickable {
                                    selectedLocation = site
                                    showLocationDialog = false
                                    Toast.makeText(context, "Observation site updated!", Toast.LENGTH_SHORT).show()
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (selectedLocation == site) PrimaryPurpleLight else Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = site,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                fontWeight = if (selectedLocation == site) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationDialog = false }) {
                    Text("Close", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun SettingsCategoryTitle(title: String, eyeComfortMode: Boolean) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Black,
        color = if (eyeComfortMode) Color(0xFFFF8888) else PrimaryPurpleLight,
        letterSpacing = 1.2.sp
    )
}

@Composable
fun PermissionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isGranted: Boolean,
    onRequestPermission: () -> Unit,
    eyeComfortMode: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(
                width = 1.dp,
                color = if (isGranted) Color(0xFF22C55E).copy(alpha = 0.3f) else PrimaryPurple.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isGranted) Color(0xFF22C55E).copy(alpha = 0.2f) else PrimaryPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isGranted) Color(0xFF4ADE80) else PrimaryPurpleLight
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isGranted) Color(0xFF15803D).copy(alpha = 0.4f) else Color(0xFFB91C1C).copy(alpha = 0.4f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isGranted) "GRANTED" else "ACTION REQUIRED",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isGranted) Color(0xFF86EFAC) else Color(0xFFFCA5A5)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (!isGranted) {
                Button(
                    onClick = onRequestPermission,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("Allow", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            } else {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Granted",
                    tint = Color(0xFF4ADE80),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    eyeComfortMode: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (eyeComfortMode) Color(0xFFFF5555) else Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryPurple,
                uncheckedThumbColor = Color.White.copy(alpha = 0.6f),
                uncheckedTrackColor = SurfaceVariantDark
            )
        )
    }
}
