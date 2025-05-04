package dev.angryl1on.vetclinic.ui.components.pickers

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.angryl1on.vetclinic.ui.R
import dev.angryl1on.vetclinic.ui.components.fields.PrimaryTextField
import dev.angryl1on.vetclinic.ui.theme.ActiveButton
import dev.angryl1on.vetclinic.ui.theme.Black
import dev.angryl1on.vetclinic.ui.theme.Inputs
import dev.angryl1on.vetclinic.ui.theme.InputsText
import dev.angryl1on.vetclinic.ui.theme.InputsUnfocusedBorder
import dev.angryl1on.vetclinic.ui.theme.Red
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme
import dev.angryl1on.vetclinic.ui.theme.White
import timber.log.Timber
import java.util.Date
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = Inputs,
        ),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text(text = stringResource(R.string.confirm), color = Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel), color = Black)
            }
        },
        content = {
            DatePicker(
                state = datePickerState,
                colors = DatePickerColors(
                    containerColor = Inputs,
                    titleContentColor = Black,
                    headlineContentColor = Black,
                    weekdayContentColor = Black,
                    subheadContentColor = Black,
                    navigationContentColor = Black,
                    yearContentColor = Black,
                    disabledYearContentColor = Color.Unspecified,
                    currentYearContentColor = Black,
                    selectedYearContentColor = White,
                    disabledSelectedYearContentColor = Color.Unspecified,
                    selectedYearContainerColor = ActiveButton,
                    disabledSelectedYearContainerColor = Color.Unspecified,
                    dayContentColor = Black,
                    disabledDayContentColor = Color.Unspecified,
                    selectedDayContentColor = White,
                    disabledSelectedDayContentColor = Color.Unspecified,
                    selectedDayContainerColor = ActiveButton,
                    disabledSelectedDayContainerColor = Color.Unspecified,
                    todayContentColor = Black,
                    todayDateBorderColor = ActiveButton,
                    dayInSelectionRangeContainerColor = Color.Unspecified,
                    dayInSelectionRangeContentColor = Color.Unspecified,
                    dividerColor = ActiveButton,
                    dateTextFieldColors = TextFieldDefaults.colors(
                        focusedTextColor = InputsText,
                        focusedContainerColor = Inputs,
                        focusedPlaceholderColor = InputsText,
                        focusedLabelColor = ActiveButton,
                        unfocusedTextColor = InputsText,
                        unfocusedPlaceholderColor = InputsText,
                        unfocusedContainerColor = Inputs,
                        unfocusedLabelColor = InputsUnfocusedBorder,
                        unfocusedIndicatorColor = InputsUnfocusedBorder,
                        errorTextColor = Red,
                        errorContainerColor = Inputs,
                        errorCursorColor = Red,
                        errorIndicatorColor = Red,
                        errorLabelColor = Red,
                        errorSupportingTextColor = Red,
                        cursorColor = InputsText
                    )
                )
            )
        }
    )
}

@Composable
fun DatePickerTextField(
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorText: String? = null,
    title: String? = null,
    initialDate: Long? = null,
    onDateSelected: (Long?) -> Unit
) {
    var selectedDate by rememberSaveable { mutableStateOf(initialDate) }
    var isDatePickerVisible by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val formattedDate = selectedDate?.let { dateFormat.format(Date(it)) } ?: ""

    // Инпут для отображения выбранной даты
    PrimaryTextField(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            isDatePickerVisible = true
        },
        title = title,
        value = formattedDate,
        placeholder = stringResource(R.string.pick_date),
        isError = isError,
        errorText = errorText,
        readOnly = true,
        isEnabled = true,
        onTextChange = { },
        trailingIcon = R.drawable.ic_date_pick,
        onTrailingIconClicked = { isDatePickerVisible = true }
    )

    // DatePicker
    if (isDatePickerVisible) {
        DatePickerModalInput(
            onDateSelected = { millis ->
                selectedDate = millis
                onDateSelected(millis)
                isDatePickerVisible = false
            },
            onDismiss = { isDatePickerVisible = false }
        )
    }
}

@Composable
@Preview
fun DatePickerModalInput() {
    VetClinicTheme {
        Scaffold { innerPadding ->
            DatePickerTextField(
                modifier = Modifier.padding(innerPadding),
                title = "Дата рождения",
                initialDate = null,
                onDateSelected = { selectedMillis ->
                    // Здесь можно залогировать или обработать выбор даты в превью
                    Timber.d("Selected date millis: $selectedMillis")
                }
            )
        }
    }
}
