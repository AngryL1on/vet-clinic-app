package dev.angryl1on.vetclinic.auth.presentation.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import dev.angryl1on.vetclinic.auth.R
import dev.angryl1on.vetclinic.auth.presentation.viewmodels.RegisterScreenState
import dev.angryl1on.vetclinic.auth.presentation.viewmodels.RegisterViewModel
import dev.angryl1on.vetclinic.common.navigation.routeName
import dev.angryl1on.vetclinic.domain.navigation.Route
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton
import dev.angryl1on.vetclinic.ui.components.fields.PasswordTextField
import dev.angryl1on.vetclinic.ui.components.fields.PhoneTextField
import dev.angryl1on.vetclinic.ui.components.fields.PrimaryTextField
import dev.angryl1on.vetclinic.ui.theme.InputsText
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumWorkSans16
import dev.angryl1on.vetclinic.ui.theme.White
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val dimensions = LocalDimensions.current

    val uiState by viewModel.state.collectAsState(initial = RegisterScreenState.Init)

    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val phone by viewModel.number.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val repeatPassword by viewModel.confirm.collectAsState()

    val firstNameError by viewModel.firstNameErr.collectAsState()
    val lastNameError by viewModel.lastNameErr.collectAsState()
    val numberError by viewModel.numberErr.collectAsState()
    val emailError by viewModel.emailErr.collectAsState()
    val passwordError by viewModel.passwordErr.collectAsState()
    val confirmError by viewModel.confirmErr.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterScreenState.Success -> {
                val route = buildString {
                    append(Route.VerifyScreen.routeName) // ← "VerifyScreen"
                    append("?email=${Uri.encode((uiState as RegisterScreenState.Success).originalEmail)}")
                    append("&masked=${Uri.encode((uiState as RegisterScreenState.Success).maskedEmail)}")
                }

                navController.navigate(route)
            }

            else -> {}
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensions.horizontalMedium),
        color = White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium)
        ) {
            Text("Личные данные", style = MediumWorkSans16.copy(color = InputsText))

            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = firstName,
                placeholder = "Введите имя",
                title = "Имя",
                isEnabled = true,
                isError = firstNameError != null,
                errorText = firstNameError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                isMaxQuantityOfCharVisible = false,
                onTextChange = viewModel::onFirstNameChanged
            )

            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = lastName,
                placeholder = "Введите Фамилию",
                title = "Фамилия",
                isEnabled = true,
                isError = lastNameError != null,
                errorText = lastNameError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                isMaxQuantityOfCharVisible = false,
                onTextChange = viewModel::onLastNameChanged
            )

            PhoneTextField(
                phone = phone,
                isError = numberError != null,
                errorText = numberError,
                onPhoneChange = viewModel::onNumberChanged
            )

            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                placeholder = stringResource(R.string.enter_email),
                title = stringResource(R.string.email),
                isEnabled = true,
                isError = emailError != null,
                errorText = emailError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                isMaxQuantityOfCharVisible = false,
                onTextChange = viewModel::onEmailChanged
            )

            PasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                placeholderText = stringResource(R.string.enter_password),
                title = "Создайте пароль",
                value = password,
                isEnabled = true,
                isError = passwordError != null,
                errorText = passwordError,
                onTextChange = viewModel::onPasswordChanged
            )

            PasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                placeholderText = "Повторите пароль",
                title = "Пароль повторно",
                value = repeatPassword,
                isEnabled = true,
                isError = confirmError != null,
                errorText = confirmError,
                onTextChange = viewModel::onConfirmChanged
            )

            PrimaryButton(
                onButtonClick = viewModel::submit,
                text = "Далее",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
