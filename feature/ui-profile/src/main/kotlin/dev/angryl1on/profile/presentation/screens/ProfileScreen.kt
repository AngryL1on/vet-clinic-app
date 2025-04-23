package dev.angryl1on.profile.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import dev.angryl1on.profile.R
import dev.angryl1on.profile.presentation.componets.button.LogoutButton
import dev.angryl1on.profile.presentation.viewmodels.ProfileScreenState
import dev.angryl1on.profile.presentation.viewmodels.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val userInfo by viewModel.userDataState.collectAsState()
    val screenState by viewModel.state.collectAsState(initial = ProfileScreenState.Init)

    when (screenState) {
        is ProfileScreenState.Loading -> CircularProgressIndicator()
        is ProfileScreenState.Error -> Text("Ошибка: ${(screenState as ProfileScreenState.Error).message}")
        is ProfileScreenState.Success -> {
            userInfo?.let {
                Column {
                    Text("Имя: ${it.firstName ?: "Не указано"}")
                    Text("Фамилия: ${it.lastName ?: "Не указано"}")
                    Text("Email: ${it.email}")
                    Text("Активен: ${if (it.enabled) "Да" else "Нет"}")
                    it.photoUrl?.let { url ->
                        AsyncImage(model = url, contentDescription = "Фото профиля")
                    }

                    LogoutButton(
                        text = stringResource(R.string.logout),
                        onButtonClick = onLogoutClick
                    )
                }
            }
        }
        else -> {}
    }

}

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen(
        onLogoutClick = {
            // Do nothing
        }
    )
}
