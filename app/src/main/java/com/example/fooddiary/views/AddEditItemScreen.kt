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
import androidx.compose.material.icons.outlined.Star
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.fooddiary.database.Category
import com.example.fooddiary.database.Item
import com.example.fooddiary.viewmodels.HomeViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooddiary.R
import com.example.fooddiary.ui.theme.customWidgets.SimpleOutlinedTextField
import kotlinx.coroutines.launch
import java.lang.Math.ceil
import java.lang.Math.floor

var itemId: Int = -1
var itemTitle: String = ""
var itemDrawableResource: Int = 0
var itemPlaceName : String = ""
var itemCategory: Category = Category.NONE
var itemSatisfaction: Float = 0.0F;
var itemIsPetFriendly: Boolean = false

val MAX_LENGTH = 30

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemScreen(navController: NavHostController,
                      homeViewModel: HomeViewModel,
                      itemId: Int?,
                      isEdit: Boolean) {
    var selectedItem: Item
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
    if (isEdit){
        homeViewModel.findItemById(itemId!!)
        selectedItem = homeViewModel.foundItem.observeAsState().value!!
        com.example.fooddiary.views.itemId = selectedItem.id
        itemTitle = selectedItem.title
        itemCategory = selectedItem.category
        itemSatisfaction = selectedItem.satisfaction
        itemDrawableResource = selectedItem.drawableResource
        itemPlaceName = selectedItem.placeName
        itemIsPetFriendly = selectedItem.isPetFriendly
    }

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
                        .padding(10.dp)
                        .verticalScroll(state = scrollState)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.baseline_food_bank_24),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(50))
                    )

                    SimpleOutlinedTextField(inputWrapper = itemTitle, labelResId = R.string.title,
                        maxLength = MAX_LENGTH,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next)
                    ) {
                        isEdited = true
                        itemTitle = it
                    }

                    SimpleOutlinedTextField(inputWrapper = itemPlaceName, labelResId = R.string.place_name,
                        maxLength = MAX_LENGTH,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next)
                    ) {
                        isEdited = true
                        itemPlaceName = it
                    }

                    RadioButtonComponent()

                    Spacer(modifier = Modifier.height(20.dp))

                    AtmosphereSlider()

                    Spacer(modifier = Modifier.height(20.dp))

                    IsPetFriendlySwitch()

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = {
                        if (isEdited) {
                            val item = Item(
                                title = itemTitle,
                                placeName = itemPlaceName,
                                category = itemCategory,
                                satisfaction = itemSatisfaction,
                                petsFriendly = itemIsPetFriendly
                            )
                            if (isEdit){
                                updateItemInDB(navController, homeViewModel, item)
                            } else {
                                addItemToDB(navController, homeViewModel, item)
                            }
                            clearAll()
                        } else {
                            coroutineScope.launch {
                                showEditMessage()
                            }
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

fun clearAll(){
    itemId = -1
    itemTitle = ""
    itemDrawableResource = 0
    itemPlaceName = ""
    itemCategory = Category.NONE
    itemSatisfaction = 0.0F;
    itemIsPetFriendly = false
}

/*
 Text(
            text = if (isEdit) "Update Details" else "Add",
            fontSize = 18.dp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )*/


@Composable
fun RadioButtonComponent(){
    val options = listOf(Category.DINNER, Category.LUNCH, Category.BREAKFAST)
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(Category.NONE)
    }
    val mContext = LocalContext.current
    Row (
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach {
            category ->
            Column (Modifier
                .fillMaxHeight()
                .selectable(
                    selected = (category == selectedOption),
                    onClick = {
                        onOptionSelected(category)
                        itemCategory = category
                        Toast
                            .makeText(mContext, category.name, Toast.LENGTH_LONG)
                            .show()
                    }
                )
                .padding(all = 16.dp)
            ){

                RadioButton(selected = (category == selectedOption),
                    onClick = { onOptionSelected(category)
                        itemCategory = category

                    })

                Text (
                    text = category.name
                )
            }
        }
    }
}


@Composable
fun AtmosphereSlider(minVal: Float = 0f, maxVal: Float = 10f) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Column(
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Text(text = "Atmosphere",
            style = MaterialTheme.typography.labelMedium)
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it
                            itemSatisfaction = sliderPosition},
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = maxVal.toInt() - minVal.toInt() + 1,
            valueRange = minVal..maxVal
        )
        Text(text = sliderPosition.toString())
    }
}

@Composable
fun IsPetFriendlySwitch() {
    var checked by remember { mutableStateOf(true) }
    Row {
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                itemIsPetFriendly = checked
            },
            thumbContent = if (checked) {
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

}