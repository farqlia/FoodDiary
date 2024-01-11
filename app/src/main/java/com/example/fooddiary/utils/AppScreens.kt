package com.example.fooddiary.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppScreens(val title: String, val route: String, var icon: ImageVector){

    object HomeScreen: AppScreens("Home", "homeScreen", Icons.Default.Home)

    object ItemDetailsScreen : AppScreens("Item Details", "itemDetailsScreen", Icons.Default.Home)


}
