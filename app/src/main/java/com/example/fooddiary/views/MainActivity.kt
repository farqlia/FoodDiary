package com.example.fooddiary.views

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fooddiary.photo_gallery.CameraView
import com.example.fooddiary.ui.theme.AppTheme
import com.example.fooddiary.utils.Permission
import com.example.fooddiary.viewmodels.HomeViewModel
import com.example.fooddiary.viewmodels.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //AppMainScreen(homeViewModel = homeViewModel)
                    MainContent(Modifier.fillMaxSize())
                }
            }
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun MainContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Permission(
        permission = android.Manifest.permission.CAMERA,
        rationale = "You said you wanted a picture, so I'm going to have to ask for permission.",
        permissionNotAvailableContent = {
            Column(modifier) {
                Text("O noes! No Camera!")
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                        )
                    }
                ) {
                    Text("Open Settings")
                }
            }
        }
    ) {
        //CameraPreview(modifier)
        CameraView(onImageCaptured = { uri, fromGallery ->
            Log.d(TAG, "Image Uri Captured from Camera View")

        }, onError = { imageCaptureException ->
            {}
        })
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
