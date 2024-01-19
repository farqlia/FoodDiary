package com.example.fooddiary.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

/*
* To link the items in a bottom navigation bar to routes in your navigation graph, it is recommended to define a sealed class,
*  such as Screen seen here, that contains the route and String resource ID for the destinations.
* */

sealed class AppScreens(val title: String, val route: String, var icon: ImageVector){

    object ListScreen: AppScreens("List of items", "homeScreen", Icons.Default.Home)

    object ItemDetailsScreen : AppScreens("Item Details", "itemDetailsScreen", Icons.Default.Home)

    object AddEditItemScreen : AppScreens("Add/Edit Item", route = "addEditItemScreen", Icons.Default.AddCircle)
    
    object SwipePhotosScreen : AppScreens("Swipe Photos", route = "swipePhotos", Icons.Default.Face)

    object TabScreen : AppScreens("Tab Screen", route = "tabScreen", Icons.Default.Home)

    object SettingsScreen : AppScreens("Settings Screen", route = "settingsScreen", Icons.Default.Home)

    fun routeWithArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

}
