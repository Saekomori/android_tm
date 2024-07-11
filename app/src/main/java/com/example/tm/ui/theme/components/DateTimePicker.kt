package com.example.tm.ui.theme.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDateTimePicker(
    context: Context,
    value: LocalDateTime?,
    onValueChange: (LocalDateTime?) -> Unit,
    label: String,
    placeholder: String?,
    visibleDelete: Boolean = false
) {
    val russianDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withLocale(Locale("ru", "RU"))
    val openDateDialog = remember { mutableStateOf(false) }
    val openTimeDialog = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf(value?.toLocalDate() ?: LocalDate.now()) }
    val selectedTime = remember { mutableStateOf(value?.toLocalTime() ?: LocalTime.now()) }

    if (openDateDialog.value) {
        val date = selectedDate.value
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedDate.value = LocalDate.of(year, month + 1, dayOfMonth)
                openDateDialog.value = false
                openTimeDialog.value = true
            },
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        ).show()
    }

    if (openTimeDialog.value) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                selectedTime.value = LocalTime.of(hourOfDay, minute)
                val newDateTime = LocalDateTime.of(selectedDate.value, selectedTime.value)
                onValueChange(newDateTime)
                openTimeDialog.value = false
            },
            selectedTime.value.hour,
            selectedTime.value.minute,
            true // 24-часовой формат
        ).show()
    }

    OutlinedTextField(
        label = { Text(label, color = MaterialTheme.colorScheme.onSurface) },
        placeholder = { Text(placeholder ?: "", color = MaterialTheme.colorScheme.onSurfaceVariant) },
        modifier = Modifier
            .clickable { openDateDialog.value = true },
        enabled = false,
        value = value?.format(russianDateFormatter) ?: "",
        onValueChange = {},
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        trailingIcon = {
            if (value != null && visibleDelete) {
                IconButton(onClick = { onValueChange(null) }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear notification", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    )
}