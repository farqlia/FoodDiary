package com.example.fooddiary.views

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fooddiary.utils.AppScreens
import com.example.fooddiary.viewmodels.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AppRouter(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    appSettingsManager : AppSettingsManager,
    userInfoManager: UserInfoManager,
    openDrawer: () -> Unit
) {
    NavHost(navController = navController, startDestination = AppScreens.ListScreen.route){
        composable(route = AppScreens.ListScreen.route) {
            ListScreen(navController, homeViewModel, openDrawer)
        }

        composable(route = AppScreens.PhotoGalleryScreen.route){
            PhotoGalleryScreen(navController, homeViewModel)
        }

        composable(route = AppScreens.TakePhotoScreen.route){
            TakePhotoScreen(navController)
        }

        composable(route = AppScreens.TabScreen.route){
            TabScreen(navController = navController, appSettingsManager = appSettingsManager,
                userInfoManager = userInfoManager)
        }

        composable(route = AppScreens.SettingsScreen.route){
            AppSettingsScreen(appSettingsManager = appSettingsManager)
        }

        composable(route = AppScreens.AddEditItemScreen.route + "?itemId={itemId}&isEdit={isEdit}",
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

        composable(route = AppScreens.PhotoPreviewScreen.route + "/{imageUri}",
            arguments = listOf(
                navArgument("imageUri"){
                    type = NavType.StringType
                }
            )
        ){
            val imageUri = it.arguments?.getString("imageUri")!!

            val scope = rememberCoroutineScope()
            scope.launch {
                userInfoManager.updateImageUri(imageUri)
            }

            PhotoPreview(imageUri = imageUri)
        }

        composable(route = AppScreens.SwipeGalleryScreen.route + "/{initialPage}",
            arguments = listOf(
                navArgument("initialPage"){
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ){
            val initialPage = it.arguments?.getInt("initialPage")!!
            SwipeGalleryPhotosScreen(navController, userInfoManager, initialPage = initialPage)
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