package com.example.fooddiary.views

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fooddiary.R
import com.example.fooddiary.database.Category
import com.example.fooddiary.database.Item
import com.example.fooddiary.ui.theme.customWidgets.SimpleOutlinedTextField
import com.example.fooddiary.viewmodels.HomeViewModel
import kotlinx.coroutines.delay

val MAX_LENGTH = 30

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemScreen(navController: NavHostController,
                      homeViewModel: HomeViewModel,
                      itemId: Int?,
                      isEdit: Boolean) {
    val selectedItem: Item
    val mContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var validationMessageShown by remember { mutableStateOf(false) }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    var item = Item()

    if (isEdit){
        homeViewModel.findItemById(itemId!!)
        item = homeViewModel.foundItem.observeAsState().value!!
    }

    var title by remember { mutableStateOf(item.title) }
    var drawableResource by remember { mutableStateOf(item.drawableResource) }
    var placeName by remember { mutableStateOf(item.placeName) }
    var category by remember { mutableStateOf(item.category) }
    var satisfaction by remember { mutableStateOf(item.satisfaction) }
    var isPetFriendly by remember { mutableStateOf(item.isPetFriendly) }


    // Shows the validation message.
    suspend fun showEditMessage() {
        if (!validationMessageShown) {
            validationMessageShown = true
            delay(3000L)
            validationMessageShown = false
        }
    }
    val scrollState = rememberScrollState()
    var isEdited by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomToolbarWithBackArrow(
                title = if (isEdit) "Edit Item" else "Add Item",
                navController = navController
            )
        },
        content = { padding ->
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(state = scrollState)
                ){
                    Image(
                        painter = painterResource(id = if (isEdit){
                            getDrawableBasedOnCategory(item.category)
                        } else {
                            R.drawable.baseline_add_task_24
                        }),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(50))
                    )

                    SimpleOutlinedTextField(inputWrapper = title, labelResId = R.string.title,
                        maxLength = MAX_LENGTH,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next)
                    ) {
                        isEdited = true
                        title = it
                    }

                    SimpleOutlinedTextField(inputWrapper = placeName, labelResId = R.string.place_name,
                        maxLength = MAX_LENGTH,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next)
                    ) {
                        isEdited = true
                        placeName = it
                    }

                    val options = listOf(Category.DINNER, Category.LUNCH, Category.BREAKFAST)
                    val (selectedOption, onOptionSelected) = remember {
                        mutableStateOf(item.category)
                    }
                    val mContext = LocalContext.current
                    Row (
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        options.forEach {
                                cat ->
                            Column (Modifier
                                .fillMaxHeight()
                                .selectable(
                                    selected = (cat == selectedOption),
                                    onClick = {
                                        onOptionSelected(cat)
                                        category = cat
                                        Toast
                                            .makeText(mContext, cat.name, Toast.LENGTH_LONG)
                                            .show()
                                    }
                                )
                                .padding(all = 16.dp)
                            ){

                                RadioButton(selected = (cat == selectedOption),
                                    onClick = { onOptionSelected(cat)
                                        category = cat

                                    })

                                Text (
                                    text = cat.name
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    var myRating by remember { mutableIntStateOf(satisfaction.toInt()) }

                    RatingBar(
                        currentRating = myRating,
                        onRatingChanged = {
                            myRating = it
                            satisfaction = myRating.toFloat()
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row {
                        Switch(
                            checked = isPetFriendly,
                            onCheckedChange = {
                                isPetFriendly = it
                            },
                            thumbContent = if (isPetFriendly) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            } else {
                                null
                            },
                            modifier = Modifier.padding(vertical = 10.dp)
                        )

                        Text(
                            text = "Is Pet Friendly",
                            style = MaterialTheme.typography.labelMedium)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.Bottom){
                        Button(onClick = {

                            item.title = title
                            item.placeName = placeName
                            item.satisfaction = satisfaction
                            item.category = category
                            item.isPetFriendly = isPetFriendly

                            if (isEdit){
                                updateItemInDB(navController, homeViewModel, item)
                            } else {
                                addItemToDB(navController, homeViewModel, item)
                            }

                        }) {
                            Text(
                                text = if (isEdit) "Update Details" else "Add",
                                fontSize = 18.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                }
            }
        }
    )
}

fun addItemToDB(navController: NavHostController,
                homeViewModel: HomeViewModel,
                item: Item){
    homeViewModel.addItem(item)
    navController.popBackStack()
}

fun updateItemInDB(navController: NavHostController,
                   homeViewModel: HomeViewModel,
                item: Item){
    homeViewModel.updateItem(item)
    navController.popBackStack()
}
