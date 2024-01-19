package com.example.fooddiary.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fooddiary.R
import kotlinx.coroutines.launch

val photos = listOf(
    R.drawable.gp, R.drawable.novio, R.drawable.spaghetti_2, R.drawable.spaghetti_3
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SwipePhotosScreen(navController: NavHostController){
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ){
        photos.size
    }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CustomToolbarWithBackArrow(
                title = "Image Gallery",
                navController = navController
            )},
            content = {paddingValues ->
            Box(modifier = Modifier.fillMaxSize()){

                HorizontalPager(state = pagerState, key = { photos[it]}, pageSize = PageSize.Fill) {
                    index ->
                    Image(painter = painterResource(id = photos[index]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize())

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
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)}
                        },
                            modifier =  Modifier.align(Alignment.CenterStart)){
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = "Go Back"
                            )
                        }
                        IconButton(onClick = { scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)} },
                            modifier =  Modifier.align(Alignment.CenterStart)){
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "Go Forward"
                            )
                        }
                    }
                }

        }

    )
}