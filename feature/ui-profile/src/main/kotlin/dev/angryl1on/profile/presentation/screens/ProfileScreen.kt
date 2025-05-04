package dev.angryl1on.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.angryl1on.profile.R
import dev.angryl1on.profile.presentation.componets.buttons.LogoutButton
import dev.angryl1on.profile.presentation.componets.buttons.section.ProfileSectionButton
import dev.angryl1on.profile.presentation.viewmodels.ProfileScreenState
import dev.angryl1on.profile.presentation.viewmodels.ProfileViewModel
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumRoboto16
import dev.angryl1on.vetclinic.ui.theme.White
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    onPersonalDataClick: () -> Unit,
    onPetisiansManagementClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val userInfo by viewModel.userDataState.collectAsState()
    val screenState by viewModel.state.collectAsState(initial = ProfileScreenState.Init)

    val dimensions = LocalDimensions.current
    when (screenState) {
        is ProfileScreenState.Loading -> CircularProgressIndicator()
        is ProfileScreenState.Error -> Text("Ошибка: ${(screenState as ProfileScreenState.Error).message}")
        is ProfileScreenState.Success -> {
            userInfo?.let {
                Column(
                    modifier = Modifier
                        .background(White)
                        .padding(horizontal = dimensions.horizontalMedium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensions.verticalXSmall)
                ) {
                    it.photoUrl?.let { url ->
                        AsyncImage(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Gray, CircleShape),
                            model = url,
                            contentDescription = stringResource(R.string.profile_photo)
                        )
                    }
                    Text(
                        text = "Email: ${it.email}",
                        style = MediumRoboto16
                    )

                    Spacer(modifier = Modifier.height(dimensions.verticalXSmall))

                    ProfileSectionButton(
                        iconSection = dev.angryl1on.vetclinic.ui.R.drawable.ic_account_file,
                        textSection = stringResource(R.string.personal_data),
                        onSectionClick = onPersonalDataClick
                    )

                    ProfileSectionButton(
                        iconSection = dev.angryl1on.vetclinic.ui.R.drawable.ic_animal_shelter,
                        textSection = stringResource(R.string.petisians_management),
                        onSectionClick = onPetisiansManagementClick
                    )

                    Spacer(modifier = Modifier.height(dimensions.verticalXSmall))

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
        },
        onPersonalDataClick = {
            // Do nothing
        },
        onPetisiansManagementClick = {
            // Do nothing
        }
    )
}
