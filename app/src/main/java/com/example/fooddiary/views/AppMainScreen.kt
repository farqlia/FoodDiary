package com.example.fooddiary.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.fooddiary.utils.AppScreens
import com.example.fooddiary.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

private val screens = listOf(
    AppScreens.ListScreen,
    AppScreens.AddEditItemScreen
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMainScreen(homeViewModel: HomeViewModel) {
    val navController = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val openDrawer = {
            scope.launch {
                drawerState.open()
            }
        }

        val onDestinationClicked = { route : String ->
            scope.launch {
                drawerState.close()
            }
            navController.navigate(route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                navController.graph.startDestinationRoute?.let { route ->
                    popUpTo(route) {
                        saveState = true
                    }
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {

                    ModalDrawerSheet {
                        Text("Drawer title", modifier = Modifier.padding(16.dp))
                        Divider()
                        screens.forEach { screen ->
                            NavigationDrawerItem(label = {Text (text=screen.title)}, selected = false, onClick = { onDestinationClicked(screen.route) })
                            Spacer(modifier = Modifier.width(7.dp))
                        }

                    }
            }){
            Scaffold(
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { Text("Show drawer") },
                        icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    )
                }
            ) {
                contentPadding ->
                AppRouter(navController = navController, homeViewModel = homeViewModel,
                    openDrawer = { openDrawer() })
                // Screen Content
            }
        }
    }
}

