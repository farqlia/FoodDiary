package com.example.fooddiary.ui.theme.customWidgets

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleOutlinedTextField(inputWrapper: String,
                            @StringRes labelResId: Int,
                            maxLength: Int,
                            keyboardOptions: KeyboardOptions = remember { KeyboardOptions.Default },
                            onTextChanged: (String) -> Unit) {
    var fieldValue by remember { mutableStateOf(inputWrapper) }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = fieldValue,
        onValueChange = {
            if (it.length <= maxLength){
                fieldValue = it
                onTextChanged(it)
            }},
        label = { Text(stringResource(labelResId), style = MaterialTheme.typography.labelMedium) },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            },
            onDone = {
                focusManager.clearFocus()
            }
        ),

    )
}