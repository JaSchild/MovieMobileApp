package com.example.capstone

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import com.example.capstone.ui.theme.CapstoneTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.capstone.ui.screens.FavouriteScreen
import com.example.capstone.ui.screens.HomeScreen
import com.example.capstone.ui.screens.InfoScreen
import com.example.capstone.ui.screens.MovieAppScreens
import com.example.capstone.ui.screens.SearchScreen
import com.example.capstone.ui.screens.SettingsScreen
import android.Manifest
import androidx.annotation.RequiresApi

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }

        createNotificationChannel(this)

        enableEdgeToEdge()
        setContent {
            CapstoneTheme {
                CapstoneApp()
            }
        }
    }
}

/**
 * Main entry point for the Capstone app.
 *
 * @RequiresApi(Build.VERSION_CODES.O) Indicates that this function requires API level 26 or higher.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CapstoneApp() {
    val navController = rememberNavController()
    var isMenuVisible by remember { mutableStateOf(false) } // Track menu visibility

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = colorResource(id = R.color.background),
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.border),
                            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                        ),
                    containerColor = colorResource(R.color.nav_bar),
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { navController.navigate(MovieAppScreens.Home.route) },
                        modifier = Modifier.size(70.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = colorResource(R.color.icons)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { navController.navigate("search/${""}") },
                        modifier = Modifier.size(70.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = colorResource(R.color.icons)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { isMenuVisible = true },
                        modifier = Modifier.size(70.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu",
                            tint = colorResource(R.color.icons)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            },
        ) { innerPadding ->
            NavigationHost(navController, innerPadding)
        }

        // Right Side Menu Overlay
        RightSideMenuScreen(navController, isMenuVisible) { isMenuVisible = false }
    }
}

/**
 * Displays the right side menu overlay.
 *
 * @param navController The NavHostController for managing navigation.
 * @param isVisible Boolean indicating if the menu is visible.
 * @param onClose Callback to close the menu.
 */
@Composable
fun RightSideMenuScreen(navController: NavHostController, isVisible: Boolean, onClose: () -> Unit, ) {

    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Covers the entire screen
                .background(Color.Black.copy(alpha = 0.5f)) // Dim background
                .pointerInput(Unit) { // Detect taps outside the menu
                    detectTapGestures { onClose() }
                }, contentAlignment = Alignment.TopEnd
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally { fullWidth -> fullWidth }, // Slide from right
                exit = slideOutHorizontally { fullWidth -> fullWidth }  // Slide out to right
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(200.dp) // Fixed width
                        .background(colorResource(id = R.color.nav_bar))
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.border),
                            shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                        )
                        .padding(5.dp),

                    ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                navController.navigate(MovieAppScreens.Settings.route)
                                onClose()
                            },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.nav_bar),
                                contentColor = Color.White,
                            ),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "Settings Icon",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Settings",
                                    fontSize = 20.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(5.dp))
                        Button(
                            onClick = {
                                navController.navigate(MovieAppScreens.Favourites.route)
                                onClose()
                            },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.nav_bar),
                                contentColor = Color.White,
                            ),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Settings Icon",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Favourites",
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Displays the main navigation host for the app.
 *
 * @param navController The NavHostController for managing navigation.
 * @param innerPadding Padding values for the content.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = MovieAppScreens.Home.route,
    ) {
        composable(MovieAppScreens.Home.route) {
            HomeScreen(navController = navController)
        }
        composable("movieDetail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toInt() ?: 0
            InfoScreen(movieId = movieId, navController = navController)
        }
        composable("${MovieAppScreens.Search.route}/{query}") { backStackEntry ->
            SearchScreen(navController = navController, navBackStackEntry = backStackEntry)
        }
        composable(MovieAppScreens.Favourites.route) {
            FavouriteScreen(
                navController = navController,
            )
        }
        composable(MovieAppScreens.Settings.route) {
            SettingsScreen()
        }
    }
}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "movie_channel_id",
            "Movie Expiry Alerts",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifies when a movie is about to leave a platform"
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}