package dev.angryl1on.vetclinic.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import coil.compose.AsyncImage
import dev.angryl1on.vetclinic.auth.R
import dev.angryl1on.vetclinic.auth.presentation.viewmodels.LoginScreenState
import dev.angryl1on.vetclinic.auth.presentation.viewmodels.LoginViewModel
import dev.angryl1on.vetclinic.domain.navigation.Route
import dev.angryl1on.vetclinic.domain.navigation.Route.LoginScreen
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton
import dev.angryl1on.vetclinic.ui.components.fields.PasswordTextField
import dev.angryl1on.vetclinic.ui.components.fields.PrimaryTextField
import dev.angryl1on.vetclinic.ui.provider.LocalSnackbarHostState
import dev.angryl1on.vetclinic.ui.provider.showMessage
import dev.angryl1on.vetclinic.ui.theme.Black
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.Red
import dev.angryl1on.vetclinic.ui.theme.RegularWorkSans24
import dev.angryl1on.vetclinic.ui.theme.White
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel()
) {
    val dimensions = LocalDimensions.current
    val screenState by viewModel.state.collectAsState(initial = LoginScreenState.Init)
    val authData by viewModel.authDataState.collectAsState()
    val snackbarHostState = LocalSnackbarHostState.current

    // При наличии ошибки валидации извлекаем текст ошибки
    val loginError: String = if (screenState is LoginScreenState.ValidationError) {
        (screenState as LoginScreenState.ValidationError).loginError ?: ""
    } else {
        ""
    }

    // Навигация или показ ошибки в зависимости от состояния экрана
    LaunchedEffect(screenState) {
        when (screenState) {
            is LoginScreenState.Success -> {
                navController.navigate(Route.MainScreen) {
                    popUpTo(LoginScreen) { inclusive = true }
                }
            }
            is LoginScreenState.Error -> {
                (screenState as LoginScreenState.Error).message?.also { message ->
                    snackbarHostState.showMessage(message = message)
                }
            }

            else -> {
                /* Do nothing */
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXSmall)
    ) {
        AsyncImage(
            model = dev.angryl1on.vetclinic.ui.R.drawable.login_image,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier.padding(horizontal = dimensions.horizontalMedium),
            verticalArrangement = Arrangement.spacedBy(dimensions.verticalXSmall)
        ) {
            Text(
                text = stringResource(R.string.welcome),
                style = RegularWorkSans24.copy(color = Black)
            )

            Spacer(modifier = Modifier.height(dimensions.verticalXSmall))

            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = authData.email,
                placeholder = stringResource(R.string.enter_email),
                isEnabled = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                isMaxQuantityOfCharVisible = false,
                onTextChange = { newEmail ->
                    viewModel.changeEmail(newEmail)
                },
                isError = loginError.isNotEmpty(),
                errorText = loginError
            )

            PasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                placeholderText = stringResource(R.string.enter_password),
                value = authData.password,
                isEnabled = true,
                isError = false,
                onTextChange = { newPassword ->
                    viewModel.changePassword(newPassword)
                }
            )
            when (screenState) {
                is LoginScreenState.Error -> {
                    Spacer(modifier = Modifier.height(dimensions.verticalMedium))
                    Text(
                        text = LoginViewModel.INCORRECT_LOGIN_OR_PASSWORD,
                        color = Red
                    )
                }

                is LoginScreenState.ValidationError -> {
                    Spacer(modifier = Modifier.height(dimensions.verticalMedium))
                    Text(
                        text = LoginViewModel.INVALID_FIELDS,
                        color = Red
                    )
                }

                else -> {
                    /* Do nothing */
                }
            }

            Spacer(modifier = Modifier.height(dimensions.verticalXSmall))

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
//                isLoading = screenState is LoginScreenState.Loading,
                onButtonClick = { viewModel.signIn() },
                text = stringResource(R.string.login)
            )
        }
    }
}

//@Composable
//@Preview
//fun LoginScreeenPreview() {
//    LoginScreen(
//
//    )
//}
