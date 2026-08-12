package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "HOME", Icons.Filled.Home)
    object Explore : Screen("explore", "EXPLORE", Icons.Filled.Search)
    object SkyMap : Screen("skymap", "SKY MAP", Icons.Filled.Map)
    object Tools : Screen("tools", "TOOLS", Icons.Filled.Build)
    object Saved : Screen("saved", "SAVED", Icons.Filled.Bookmark)
    object Settings : Screen("settings", "SETTINGS", Icons.Filled.Settings)
    object About : Screen("about", "ABOUT", Icons.Filled.Info)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Explore,
    Screen.SkyMap,
    Screen.Tools,
    Screen.Saved
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AstronomyApp(viewModel: AstronomyViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showNotificationModal by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = SurfaceCosmicDark,
                drawerContentColor = Color.White
            ) {
                AppDrawerContent(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (currentRoute != Screen.Settings.route && currentRoute != Screen.About.route) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "ASTRONOMYZ",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp,
                                color = Color.White
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    Icons.Filled.Menu,
                                    contentDescription = "Menu",
                                    tint = Color.White
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { showNotificationModal = true }) {
                                Box {
                                    Icon(
                                        Icons.Filled.Notifications,
                                        contentDescription = "Notifications",
                                        tint = PrimaryPurpleLight
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFEF4444))
                                            .align(Alignment.TopEnd)
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = DeepCosmicDark
                        )
                    )
                }
            },
            bottomBar = {
                if (currentRoute != Screen.Settings.route && currentRoute != Screen.About.route) {
                    AppBottomNavigation(navController = navController)
                }
            },
            containerColor = DeepCosmicDark
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding),
                enterTransition = { fadeIn(animationSpec = tween(220)) + slideInHorizontally(animationSpec = tween(220)) { it / 6 } },
                exitTransition = { fadeOut(animationSpec = tween(220)) + slideOutHorizontally(animationSpec = tween(220)) { -it / 6 } },
                popEnterTransition = { fadeIn(animationSpec = tween(220)) + slideInHorizontally(animationSpec = tween(220)) { -it / 6 } },
                popExitTransition = { fadeOut(animationSpec = tween(220)) + slideOutHorizontally(animationSpec = tween(220)) { it / 6 } }
            ) {
                composable(Screen.Home.route) { HomeScreen(viewModel, onNavigateToSkyMap = { navController.navigate(Screen.SkyMap.route) }) }
                composable(Screen.Explore.route) { ExploreScreen(viewModel) }
                composable(Screen.SkyMap.route) { SkyMapScreen() }
                composable(Screen.Tools.route) { ToolsScreen() }
                composable(Screen.Saved.route) { SavedScreen(viewModel) }
                composable(Screen.Settings.route) { SettingsScreen(onNavigateBack = { navController.popBackStack() }) }
                composable(Screen.About.route) { AboutScreen(onNavigateBack = { navController.popBackStack() }) }
            }
        }
    }

    if (showNotificationModal) {
        NotificationCenterDialog(onDismiss = { showNotificationModal = false })
    }
}

@Composable
fun AppBottomNavigation(navController: NavHostController) {
    NavigationBar(
        containerColor = SurfaceCosmicDark,
        tonalElevation = 0.dp,
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavItems.forEach { screen ->
            val isSelected = currentRoute == screen.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                        tint = if (isSelected) PrimaryPurpleLight else Color.White.copy(alpha = 0.4f)
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) PrimaryPurpleLight else Color.White.copy(alpha = 0.4f)
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = SurfaceVariantDark,
                    selectedIconColor = PrimaryPurpleLight,
                    unselectedIconColor = Color.White.copy(alpha = 0.4f),
                    selectedTextColor = PrimaryPurpleLight,
                    unselectedTextColor = Color.White.copy(alpha = 0.4f)
                )
            )
        }
    }
}

@Composable
fun AppDrawerContent(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = "ASTRONOMYZ",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Black,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
    Text(
        text = "Cosmic Sky Guide & Astronomy Tools",
        style = MaterialTheme.typography.bodySmall,
        color = PrimaryPurpleLight,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))
    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { DrawerItem("Home", Icons.Filled.Home, currentRoute == Screen.Home.route) { onNavigate(Screen.Home.route) } }
        item { DrawerItem("Sky Map", Icons.Filled.Map, currentRoute == Screen.SkyMap.route) { onNavigate(Screen.SkyMap.route) } }
        item { DrawerItem("Explore & Planets", Icons.Filled.Public, currentRoute == Screen.Explore.route) { onNavigate(Screen.Explore.route) } }
        item { DrawerItem("Astronomy Tools", Icons.Filled.Build, currentRoute == Screen.Tools.route) { onNavigate(Screen.Tools.route) } }
        item { DrawerItem("Saved Articles", Icons.Filled.Bookmark, currentRoute == Screen.Saved.route) { onNavigate(Screen.Saved.route) } }
        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.White.copy(alpha = 0.1f)) }
        item { DrawerItem("Settings & Permissions", Icons.Filled.Settings, currentRoute == Screen.Settings.route) { onNavigate(Screen.Settings.route) } }
        item { DrawerItem("About AstronomyZ", Icons.Filled.Info, currentRoute == Screen.About.route) { onNavigate(Screen.About.route) } }
    }
}

@Composable
fun DrawerItem(title: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (isSelected) PrimaryPurple.copy(alpha = 0.25f) else Color.Transparent)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = if (isSelected) PrimaryPurpleLight else Color.White.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
        )
    }
}

@Composable
fun NotificationCenterDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCosmicDark,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = PrimaryPurpleLight,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "CELESTIAL ALERTS",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NotificationAlertCard(
                    title = "Jupiter at Opposition",
                    time = "Tonight • 10:15 PM",
                    description = "Jupiter reaches its highest & brightest point in the sky tonight. Excellent telescope visibility!",
                    icon = Icons.Default.Public
                )
                NotificationAlertCard(
                    title = "Eta Aquariids Meteor Peak",
                    time = "Tomorrow Morning • 3:00 AM",
                    description = "Up to 50 swift meteors per hour visible under clear dark skies.",
                    icon = Icons.Default.AutoAwesome
                )
                NotificationAlertCard(
                    title = "ISS Bright Pass Overhead",
                    time = "May 25 • 8:42 PM",
                    description = "International Space Station passing SW to NE at magnitude -3.8.",
                    icon = Icons.Default.RocketLaunch
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss", color = PrimaryPurpleLight, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun NotificationAlertCard(
    title: String,
    time: String,
    description: String,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(PrimaryPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryPurpleLight,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = time, style = MaterialTheme.typography.labelSmall, color = PrimaryPurpleLight, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}


