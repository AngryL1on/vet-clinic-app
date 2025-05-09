package dev.angryl1on.vetclinic.auth.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import dev.angryl1on.vetclinic.auth.presentation.viewmodels.VerifyRegisterViewModel
import dev.angryl1on.vetclinic.auth.presentation.viewmodels.VerifyScreenState
import dev.angryl1on.vetclinic.domain.navigation.Route
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton
import dev.angryl1on.vetclinic.ui.components.fields.PrimaryTextField
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.White
import org.koin.androidx.compose.koinViewModel

@Composable
fun VerifyScreen(
    navController: NavController,
    viewModel: VerifyRegisterViewModel = koinViewModel()
) {
    val dimensions = LocalDimensions.current

    val uiState by viewModel.state.collectAsState(initial = VerifyScreenState.Init)
    val code    by viewModel.code.collectAsState()
    val codeErr by viewModel.codeErr.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is VerifyScreenState.Success) {
            navController.navigate(Route.StartScreen) {
                popUpTo(Route.RegistrationScreen) { inclusive = true }
            }
        }
    }

    Surface(
        modifier = Modifier.padding(horizontal = dimensions.horizontalMedium),
        color = White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.verticalXSmall)
        ) {
            PrimaryTextField(
                title = "Проверочный код",
                placeholder = "Введите код из письма",
                value = code,
                isError = codeErr != null,
                isOnlyNumbers = true,
                errorText = codeErr,
                onTextChange = viewModel::onCodeChanged
            )

            PrimaryButton(
                text = "Подтвердить регистрацию",
                onButtonClick = viewModel::submit
            )
        }
    }

}
