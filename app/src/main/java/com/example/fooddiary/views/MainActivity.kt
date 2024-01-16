package com.example.fooddiary.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.fooddiary.R
import com.example.fooddiary.database.Item
import com.example.fooddiary.ui.theme.FoodDiaryTheme
import com.example.fooddiary.viewmodels.HomeViewModel
import com.example.fooddiary.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodDiaryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppMainScreen(homeViewModel = homeViewModel)
                }
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun ListItemPreview() {
    FoodDiaryTheme {
        Surface{
            ItemCard(item = Item(drawableResource = R.drawable.baseline_coffee_24,
                title = "Lunch at coffee shop", placeName = "Street Bacacay"),
                rememberNavController())
        }
    }
}*/
