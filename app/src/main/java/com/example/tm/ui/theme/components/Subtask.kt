package com.example.tm.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tm.data.SubTask


@Composable
fun SubTaskAdd(
    onAddSubtaskClick: (String) -> Unit,
) {
    var textFieldValue by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                onAddSubtaskClick(textFieldValue)
                textFieldValue = ""
            },
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Добавление")
        }
        CustomTextField(
            value = textFieldValue,
            onValueChange = { newValue -> textFieldValue = newValue },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun SubTaskText(
    subTask: SubTask,
    onTextChanged: (String) -> Unit,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    onDelete: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = subTask.completed,
            onClick = { onSelectedChange(!subTask.completed) },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurface
            )
        )
        CustomTextField(
            value = subTask.text,
            onValueChange = { onTextChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary)
        )
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(24.dp) // Adjust size as needed
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Delete SubTask",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = modifier
            .height(40.dp)
            .padding(start = 10.dp, top = 10.dp)
    )
}