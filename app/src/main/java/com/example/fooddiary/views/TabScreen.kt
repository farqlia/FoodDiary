package com.example.fooddiary.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabScreen(navController: NavHostController, appSettingsManager: AppSettingsManager){
    val tabItems = listOf(
        TabItem("Home", Icons.Outlined.Home, Icons.Filled.Home),
        TabItem("Settings", Icons.Outlined.Settings, Icons.Filled.Settings),
        TabItem("Your Profile", Icons.Outlined.AccountCircle, Icons.Filled.AccountCircle)
    )
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    Scaffold(
        topBar = {
            CustomToolbarWithBackArrow(
                title = "App",
                navController = navController
            )},
        content = {paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    TabRow(selectedTabIndex = selectedTabIndex){
                        tabItems.forEachIndexed { index, item ->
                            Tab(selected = index == selectedTabIndex,
                                onClick = {
                                    selectedTabIndex = index
                                },
                                text = {
                                    Text(text = item.title)
                                },
                                icon = {
                                    Icon(imageVector = if (index == selectedTabIndex) {
                                        item.selectedIcon } else item.unselectedIcon,
                                        contentDescription = item.title)
                                }
                            )
                        }
                    }
                    when (selectedTabIndex) {
                        1 -> AppSettingsScreen(appSettingsManager)
                    }
                }
            }
        }
    )
}

data class TabItem(
    val title : String,
    val unselectedIcon : ImageVector,
    val selectedIcon : ImageVector
)
