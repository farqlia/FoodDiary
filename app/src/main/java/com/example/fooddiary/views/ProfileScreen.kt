package com.example.fooddiary.views

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.fooddiary.viewmodels.ProfileScreenState
import com.example.fooddiary.views.UserInfoManager.InfoKeys.IMAGE_URI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class UserInfoManager(context: Context){

    private val dataStore = context.dataStore

    val userInfoFlow : Flow<ProfileScreenState> = dataStore.data.map {  preferences ->
        val firstName = preferences[InfoKeys.FIRST_NAME] ?: ""
        val surName = preferences[InfoKeys.SUR_NAME] ?: ""
        val description = preferences[InfoKeys.DESCRIPTION] ?: ""
        ProfileScreenState(firstName, surName, description)
    }

    val imageFlow : Flow<String> = dataStore.data.map { preferences ->
        val imageUri = preferences[InfoKeys.IMAGE_URI] ?: ""
        imageUri
    }

    suspend fun updateImageUri(imageUri: String) {
        dataStore.edit { preferences ->
            preferences[InfoKeys.IMAGE_URI] = imageUri
        }
    }

    suspend fun updateUserProfile(firstName: String, surName: String, description: String){
        dataStore.edit { preferences ->
            preferences[InfoKeys.FIRST_NAME] = firstName
            preferences[InfoKeys.SUR_NAME] = surName
            preferences[InfoKeys.DESCRIPTION] = description
        }
    }

    private object InfoKeys {
        val FIRST_NAME = stringPreferencesKey("first_name")
        val SUR_NAME = stringPreferencesKey("sur_name")
        val DESCRIPTION = stringPreferencesKey("description")
        val IMAGE_URI = stringPreferencesKey("image_uri")
    }
}

@Composable
fun UserInfoScreen(userInfoManager: UserInfoManager) {
    var firstName by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userInfoManager.userInfoFlow){
        userInfoManager.userInfoFlow.collect { userInfo ->
            firstName = userInfo.firstName
            surname = userInfo.surName
            description = userInfo.description
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Input fields for first name, surname, and description
        UserInfoTextField("First Name", Icons.Default.AccountCircle, firstName) {
            firstName = it
        }

        Spacer(modifier = Modifier.height(8.dp))

        UserInfoTextField("Surname", Icons.Default.AccountCircle, surname) {
            surname = it
        }

        Spacer(modifier = Modifier.height(8.dp))

        UserInfoTextField("Description", Icons.Default.Settings, description) {
            description = it

        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to submit the information
        Button(onClick = {
            coroutineScope.launch {
                userInfoManager.updateUserProfile(firstName, surname, description)
            }
            // Handle the submission logic here
            // You can use the values of firstName, surname, and description
        }) {
            Text("Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoTextField(
    label: String,
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = androidx.compose.ui.text.input.ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        textStyle = MaterialTheme.typography.bodyMedium
    )
}