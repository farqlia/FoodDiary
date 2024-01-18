package com.example.fooddiary.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fooddiary.R
import com.example.fooddiary.database.Item
import com.example.fooddiary.utils.AppScreens
import com.example.fooddiary.viewmodels.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemCard(item: Item, navController : NavHostController,
             homeViewModel: HomeViewModel){
    Row(modifier = Modifier
        .padding(all = 8.dp)
        .combinedClickable(
            onClick = {
                navController.navigate(
                    AppScreens.ItemDetailsScreen.routeWithArgs(
                        item.id.toString()
                    )
                )
            },
        )){

        Image(
            painter = painterResource(id = R.drawable.baseline_dinner_dining_24),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = item.title,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall)

            Spacer(modifier = Modifier.height(4.dp))

            Surface(shape = MaterialTheme.shapes.medium, shadowElevation = 1.dp){
                Text(text = "Visit at ${item.placeName}",
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ItemDetailsScreen(navController : NavHostController,
                homeViewModel : HomeViewModel,
                itemId: Int?){

    homeViewModel.findItemById(itemId!!)
    val selectedItem = homeViewModel.foundItem.observeAsState().value
    val showDialog = remember { mutableStateOf(false) }

    if (selectedItem != null){
        Surface(color = Color.White, modifier = Modifier.fillMaxSize()){
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){

                    Text(
                        text = selectedItem.title,
                        style=MaterialTheme.typography.titleLarge,
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // TODO : get real drawable
                    Image(painter = painterResource(id = R.drawable.baseline_food_bank_24),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(50)),
                        )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Meal took place at ${selectedItem.placeName}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RatingBar(
                        currentRating = selectedItem.satisfaction.toInt(),
                        onRatingChanged = {},
                        clickable = false
                    )

                }
                Spacer(modifier = Modifier.weight(1f))
                Row(){
                    if (showDialog.value){
                        Alert(
                            navController = navController,
                            homeViewModel = homeViewModel,
                            selectedItem = selectedItem,
                            name = "",
                            showDialog = showDialog.value,
                            onDismiss = { showDialog.value = false }
                        )
                    }
                    Button(onClick = {
                        navController.navigate(
                            AppScreens.AddEditItemScreen.route + "?itemId=" + selectedItem.id.toString() + "&isEdit=" + true.toString())
                                     },
                        modifier = Modifier
                            .weight(1f).width(15.dp)
                    ) {
                        Text(text = "Update", fontSize = 16.sp)
                    }
                    Button(onClick = {
                        showDialog.value = true
                        },
                        modifier = Modifier
                            .weight(1f).width(15.dp)) {
                            Text(text = "Delete", fontSize = 16.sp)
                        }

                }
            }
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navController : NavHostController,
    homeViewModel : HomeViewModel,
    openDrawer : () -> Unit
){
    homeViewModel.getAllItems()
    val lazyListState = rememberLazyListState()
    Scaffold (
        topBar = {
        CustomToolbar(title = "Food Diary app", openDrawer)
    },
        content = { it ->
            val itemsList : List<Item> by homeViewModel.itemsList.observeAsState(initial = listOf())
            if (itemsList.isNotEmpty()){

                Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.padding(vertical = 4.dp),
                        state = lazyListState
                    ) {
                        items(items = itemsList) { item ->
                            ItemCard(item = item, navController = navController, homeViewModel = homeViewModel)
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        "No opinions yet.",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        textAlign = TextAlign.Center
                    )
                }
            }
    })
}


@Composable
fun Alert(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    selectedItem: Item,
    name: String,
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            title = {
                Text("Delete Item", style = MaterialTheme.typography.bodyMedium)
            },
            text = {
                Text(text = name)
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    homeViewModel.deleteItem(selectedItem)
                    navController.popBackStack()
                }) {
                    Text("DELETE")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("CANCEL")
                }
            }
        )
    }
}