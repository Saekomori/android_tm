package com.example.tm.ui.theme.components

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.CalendarPopup
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
fun CustomDatePicker(
    value: LocalDateTime,
    onValueChange: (LocalDateTime) -> Unit,
    label: String
) {
    val disabledDates = listOf(
        LocalDate.now().minusDays(7),
        LocalDate.now().minusDays(12),
        LocalDate.now().plusDays(3),
    )
    val russianDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withLocale(Locale("ru", "RU"))
    val openDateDialog = remember { mutableStateOf(false) }
    val openTimeDialog = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf(value.toLocalDate()) }
    val selectedTime = remember { mutableStateOf(value.toLocalTime()) }

    if (openDateDialog.value) {
        CalendarDialog(
            state = rememberUseCaseState(visible = true, onCloseRequest = { openDateDialog.value = false }),
            config = CalendarConfig(
                yearSelection = true,
                monthSelection = true,
                style = CalendarStyle.MONTH,
                disabledDates = disabledDates
            ),
            selection = CalendarSelection.Date(
                selectedDate = value.toLocalDate()
            ) { newDate ->
                selectedDate.value = newDate
                openDateDialog.value = false
                openTimeDialog.value = true // Open time dialog after date is selected
            }
        )
    }


    OutlinedTextField(
        label = { Text(label, color = Color.Black) },
        modifier = Modifier
            .clickable { openDateDialog.value = true }
            .padding(10.dp),
        enabled = false,
        value = value.format(russianDateFormatter),
        onValueChange = {},
        textStyle = TextStyle(color = Color.Black),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            disabledBorderColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black,
            disabledLabelColor = Color.Black
        )
    )
}


