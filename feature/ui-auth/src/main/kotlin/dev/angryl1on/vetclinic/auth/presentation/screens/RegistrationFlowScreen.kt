package dev.angryl1on.vetclinic.auth.presentation.screens

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.angryl1on.vetclinic.auth.R
import dev.angryl1on.vetclinic.ui.components.buttons.OutlinePrimaryButton
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton
import dev.angryl1on.vetclinic.ui.components.fields.PasswordTextField
import dev.angryl1on.vetclinic.ui.components.fields.PrimaryTextField
import dev.angryl1on.vetclinic.ui.components.pickers.DatePickerTextField
import dev.angryl1on.vetclinic.ui.theme.Blue
import dev.angryl1on.vetclinic.ui.theme.InputsText
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumWorkSans16
import dev.angryl1on.vetclinic.ui.theme.UnactiveButtonAndProgress
import dev.angryl1on.vetclinic.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun RegistrationFlowScreen() {
    val dimensions = LocalDimensions.current
    var currentStep by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
            .padding(horizontal = dimensions.horizontalMedium, vertical = dimensions.verticalMedium)
    ) {
        LinearProgressIndicator(
            progress = { (currentStep + 1) / 2f },
            trackColor = UnactiveButtonAndProgress,
            color = Blue,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.linearProgressIndicatorHeight),
        )

        Spacer(modifier = Modifier.height(dimensions.verticalMedium))

        when (currentStep) {
            0 -> PersonalDataScreen(onNext = { currentStep = 1 })
            1 -> PetDataScreen(
                onSubmit = { /* Handle submit */ },
                onBack = { currentStep = 0 }
            )
        }
    }
}

@Composable
fun PersonalDataScreen(onNext: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    val dimensions = LocalDimensions.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXSmall)
    ) {
        Spacer(modifier = Modifier.height(dimensions.verticalMedium))

        Text("Личные данные", style = MediumWorkSans16.copy(color = InputsText))

        Spacer(modifier = Modifier.height(dimensions.verticalXSmall))

        PrimaryTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            placeholder = stringResource(R.string.enter_email),
            title = "Электронная почта",
            isEnabled = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            isMaxQuantityOfCharVisible = false,
            onTextChange = { newEmail ->
                email = newEmail
            },
        )

        PasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            placeholderText = stringResource(R.string.enter_password),
            title = "Создайте пароль",
            value = password,
            isEnabled = true,
            isError = false,
            onTextChange = { newPassword ->
                password = newPassword
            }
        )

        PasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            placeholderText = "Повторите пароль",
            title = "Пароль повторно",
            value = repeatPassword,
            isEnabled = true,
            isError = false,
            onTextChange = { newPassword ->
                repeatPassword = newPassword
            }
        )


        Spacer(modifier = Modifier.height(dimensions.verticalXSmall))

        PrimaryButton(
            onButtonClick = onNext,
            text = "Далее",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun PetDataScreen(
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val dimensions = LocalDimensions.current
    val calendar = remember { Calendar.getInstance() }
    val onDateSelected by rememberUpdatedState(newValue = { year: Int, month: Int, day: Int ->
        birthDate = "%02d.%02d.%04d".format(day, month + 1, year)
    })
    remember(context) {
        DatePickerDialog(
            context,
            { _, year, month, day -> onDateSelected(year, month, day) },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        photoUri = uri
    }
    remember { MutableInteractionSource() }

    BackHandler {
        onBack()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(dimensions.verticalMedium))

        Text("Добавление питомца", style = MediumWorkSans16.copy(color = InputsText))

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            title = "Имя питомца",
            placeholder = "Введите имя питомца",
            isEnabled = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            isMaxQuantityOfCharVisible = false,
            onTextChange = { name = it },
        )

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerTextField(
            title = "Дата рождения",
            initialDate = null,
            onDateSelected = { millis ->
                millis?.let {
                    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    birthDate = formatter.format(Date(it))
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrimaryTextField(
            modifier = Modifier.fillMaxWidth(),
            value = breed,
            title = "Порода",
            placeholder = "Введите породу питомца",
            isEnabled = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            isMaxQuantityOfCharVisible = false,
            onTextChange = { breed = it },
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinePrimaryButton(
            text = "Загрузить",
            leftIcon = dev.angryl1on.vetclinic.ui.R.drawable.ic_upload_data,
            onButtonClick = { photoPickerLauncher.launch("image/*") }
        )

        photoUri?.let {
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = it,
                contentDescription = "Фото питомца",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            onButtonClick = onSubmit,
            text = "Готово",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
@Preview
fun RegisterScreenPreview() {
    RegistrationFlowScreen()
}
