package com.example.fooddiary.views

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fooddiary.utils.AppScreens
import com.example.fooddiary.viewmodels.HomeViewModel

@Composable
fun AppRouter(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    openDrawer: () -> Unit
) {
    NavHost(navController = navController, startDestination = AppScreens.ListScreen.route){
        composable(route = AppScreens.ListScreen.route) {
            ListScreen(navController, homeViewModel, openDrawer)
        }

        composable(route = AppScreens.AddEditItemScreen.route + "?itemId=itemId&isEdit=isEdit",
            arguments = listOf(
                navArgument("itemId"){
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("isEdit"){
                    type = NavType.BoolType
                    defaultValue = false
                }
            ),
            enterTransition = {
                // Let's make for a really long fade in
                slideInVertically(
                    initialOffsetY = { 1800 }
                )
            }, popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { 1800 }
                )
            }
        ) {
            val isEdit = it.arguments?.getBoolean("isEdit")
            val itemId = it.arguments?.getInt("itemId")
            AddEditItemScreen(navController = navController, homeViewModel = homeViewModel, itemId = itemId, isEdit = isEdit!!)
        }

        composable(route = AppScreens.ItemDetailsScreen.route + "/{itemId}",
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )) {
            val itemId = it.arguments?.getInt("itemId")
            ItemDetailsScreen(navController, homeViewModel, itemId!!)
        }
    }
}