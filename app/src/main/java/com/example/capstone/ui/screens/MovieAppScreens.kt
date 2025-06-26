package com.example.capstone.ui.screens

sealed class MovieAppScreens(
    val route: String,
) {
    object Home : MovieAppScreens("home")
    object Search : MovieAppScreens("search")
    object Favourites : MovieAppScreens("favourites")
    object Info : MovieAppScreens("info")
    object Settings : MovieAppScreens("settings")
}