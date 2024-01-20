package com.example.fooddiary.views

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MonochromePhotos
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.fooddiary.photo_gallery.CameraView
import com.example.fooddiary.utils.AppScreens
import com.example.fooddiary.utils.Permission
import com.example.fooddiary.viewmodels.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PhotoGalleryScreen(navController: NavHostController,
                       homeViewModel: HomeViewModel) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CustomToolbarWithBackArrow(
                title = "Photo Gallery",
                navController = navController
            )
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Take Image") },
                icon = { Icon(Icons.Filled.MonochromePhotos, contentDescription = "") },
                onClick = {
                    scope.launch {
                        navController.navigate(AppScreens.TakePhotoScreen.route)
                    }
                }
            )
        },
        content = {paddingValues -> /* */

            val context = LocalContext.current
            Permission(
                permission = Manifest.permission.READ_MEDIA_IMAGES,
                rationale = "You said you wanted a picture, so I'm going to have to ask for permission.",
                permissionNotAvailableContent = {
                    PermissionRequiredMessage()
                }
            ) {
                //CameraPreview(modifier)
                PhotoGrid(Modifier.padding(paddingValues), navController)
            }

        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGrid(modifier: Modifier, navController : NavHostController) {
    val context = LocalContext.current
    val listState = rememberLazyGridState()


        // Permission granted, read gallery images
    val imageList = getGalleryImages(context.contentResolver)

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        state = listState,
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
    ) {
        items(imageList.size) { i ->
            Card(onClick = {
                Toast
                    .makeText(context, "Image $i clicked!", Toast.LENGTH_LONG)
                    .show()
                navController.navigate(
                    AppScreens.SwipeGalleryScreen.routeWithArgs(
                        i.toString()
                    )
                )
            }){
                Column(
                    // on below line we are adding padding
                    // padding for our column and filling the max size.
                    Modifier
                        .fillMaxSize()
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    // on below line we are adding
                    // horizontal alignment for our column
                    horizontalAlignment = Alignment.CenterHorizontally,
                    // on below line we are adding
                    // vertical arrangement for our column
                    verticalArrangement = Arrangement.Center
                ){
                    PhotoItem(imageUri = imageList[i],
                        Modifier
                            .size(128.dp)
                            .clip(shape = MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.onBackground))
                }
            }
        }
    }
}

@Composable
fun PermissionRequiredMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Permission required to access gallery",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please grant the necessary permission in the app settings",
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoPreview(imageUri: String){
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
    ){ padding ->
        PhotoItem(imageUri = imageUri,
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground))
    }
}

@Composable
fun PhotoItem(imageUri: String, modifier : Modifier) {
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = imageUri)
                .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                }).build()
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillBounds
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SwipeGalleryPhotosScreen(navController: NavHostController, userInfoManager: UserInfoManager,
                      initialPage : Int = 0){

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    // Permission granted, read gallery images
    val imageList = getGalleryImages(context.contentResolver)

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        initialPageOffsetFraction = 0f
    ){
        imageList.size
    }

    LaunchedEffect(pagerState){
        snapshotFlow { pagerState.currentPage }.collect {page ->
            coroutineScope.launch {
                userInfoManager.updateImageUri(imageList[page])
            }
        }
    }

    Scaffold(
        topBar = {
            CustomToolbarWithBackArrow(
                title = "Image Gallery",
                navController = navController
            )},
        content = {paddingValues ->
            Box(modifier = Modifier.fillMaxSize()){

                HorizontalPager(state = pagerState, key = { imageList[it]}, pageSize = PageSize.Fill) {
                        index ->
                    PhotoPreview(imageUri = imageList[index])
                }
                Box(modifier = Modifier
                    .offset(y = -(16).dp)
                    .fillMaxWidth(0.5f)
                    .clip(
                        RoundedCornerShape(100)
                    )
                    .background(MaterialTheme.colorScheme.background)
                    .padding(8.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)}
                    },
                        modifier =  Modifier.align(Alignment.CenterStart)){
                        Icon(
                            imageVector = Icons.Default.ArrowLeft,
                            contentDescription = "Go Back"
                        )
                    }
                    IconButton(onClick = { coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)} },
                        modifier =  Modifier.align(Alignment.CenterStart)){
                        Icon(
                            imageVector = Icons.Default.ArrowRight,
                            contentDescription = "Go Forward"
                        )
                    }
                }
            }

        }

    )
}


fun getGalleryImages(contentResolver: ContentResolver): List<String> {
    val imageList = mutableListOf<String>()
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_TAKEN
    )

    val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

    val query = contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )

    query?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )
            imageList.add(contentUri.toString())
        }
    }

    return imageList
}

