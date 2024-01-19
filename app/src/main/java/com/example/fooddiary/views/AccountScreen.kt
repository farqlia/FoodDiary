package com.example.fooddiary.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fooddiary.viewmodels.ProfileScreenState
import com.example.fooddiary.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavHostController,
                  profileViewMode: ProfileViewModel,
                  openDrawer: () -> Unit,
                  userProfileInfo : ProfileScreenState) {

    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            CustomToolbarWithBackArrow(
                title = "Account Settings",
                navController = navController
            )},
        content = {paddingValues ->  
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .verticalScroll(state = scrollState)
                ){

                }
            }
        }
        
    )

}