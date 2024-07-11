package com.example.tm.ui.theme.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tm.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOutlinedTextField(
    text: String,
    placeholder: String,
    label: String,
    onTextChanged: (String) -> Unit,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
    errorMessage: String? = null,
    maxLength: Int? = null,
    modifier: Modifier = Modifier.padding(16.dp),
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                if (maxLength == null || it.length <= maxLength) {
                    onTextChanged(it)
                }
            },
            placeholder = { Text(placeholder) },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = errorMessage != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                errorLabelColor = MaterialTheme.colorScheme.error,
            )
        )
        if (errorMessage != null) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordOutlinedTextField(
    text: String,
    placeholder: String,
    label: String,
    onTextChanged: (String) -> Unit,
    keyboardType: KeyboardType,
    isPassword: Boolean = true,
    errorMessage: String? = null,
    maxLength: Int? = null,
    modifier: Modifier = Modifier.padding(16.dp),
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    val icon = if (passwordVisibility)
        painterResource(id = R.drawable.ic_visibility_icon)
    else
        painterResource(id = R.drawable.ic_off_visibility_icon)

    Column(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                if (maxLength == null || it.length <= maxLength) {
                    onTextChanged(it)
                }
            },
            placeholder = { Text(placeholder) },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        painter = icon,
                        contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            isError = errorMessage != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                errorLabelColor = MaterialTheme.colorScheme.error,
            )
        )
        if (errorMessage != null) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskText(
    text: String,
    placeholder: String,
    label: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(70.dp),
    textDecoration: TextDecoration? = null
) {
    val decorationStyle = when (textDecoration) {
        TextDecoration.Underline -> TextStyle(textDecoration = TextDecoration.Underline)
        TextDecoration.LineThrough -> TextStyle(textDecoration = TextDecoration.LineThrough)
        else -> TextStyle() // Default empty style if no decoration specified
    }

    OutlinedTextField(
        value = text,
        onValueChange = { onTextChanged(it) },
        modifier = modifier,
        placeholder = { Text(placeholder, style = TextStyle(fontSize = 18.sp)) },
        label = { Text(label, style = TextStyle(fontSize = 18.sp)) },
        textStyle = TextStyle(fontSize = 18.sp).merge(decorationStyle), // Merge with decoration style
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

