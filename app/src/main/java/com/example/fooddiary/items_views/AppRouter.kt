package com.example.fooddiary.items_views

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.fooddiary.utils.AppScreens
import com.example.fooddiary.viewmodels.HomeViewModel

@Composable
fun AppRouter(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    openDrawer: () -> Unit
) {
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen.route){
        
    }
}