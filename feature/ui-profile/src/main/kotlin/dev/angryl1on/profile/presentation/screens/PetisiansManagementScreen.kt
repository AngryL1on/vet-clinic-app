package dev.angryl1on.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.angryl1on.profile.R
import dev.angryl1on.profile.presentation.componets.cards.PetCard
import dev.angryl1on.profile.presentation.viewmodels.PetisiansManagementScreenState
import dev.angryl1on.profile.presentation.viewmodels.PetisiansManagementViewModel
import dev.angryl1on.profile.presentation.viewmodels.ProfileScreenState
import dev.angryl1on.vetclinic.model.pet.PetResponse
import dev.angryl1on.vetclinic.ui.components.dialogs.CustomAlertDialog
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.White
import org.koin.androidx.compose.koinViewModel

@Composable
fun PetisiansManagementScreen(
    viewModel: PetisiansManagementViewModel = koinViewModel(),
    onEditClick: (PetResponse) -> Unit
) {
    val petsInfo by viewModel.petsDataState.collectAsState()
    val screenState by viewModel.state.collectAsState(initial = ProfileScreenState.Init)
    val dimensions = LocalDimensions.current

    var petToDelete by remember { mutableStateOf<PetResponse?>(null) }

    if (petToDelete != null) {
        CustomAlertDialog(
            titleText = stringResource(R.string.delete_pet_question),
            bodyText = stringResource(R.string.delete_pet_question_expanded),
            confirmText = stringResource(R.string.delete),
            dismissText = stringResource(R.string.cancel),
            onDismissRequest = { petToDelete = null },
            onConfirmButtonClick = {
                petToDelete?.id?.let { viewModel.deletePet(it) }
                petToDelete = null
            },
            onDismissButtonClick = { petToDelete = null }
        )
    }

    when (screenState) {
        is PetisiansManagementScreenState.Error -> Text("Ошибка: ${(screenState as PetisiansManagementScreenState.Error).message}")

        is PetisiansManagementScreenState.Loading -> Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }

        is PetisiansManagementScreenState.Success -> {
            petsInfo.let { petsData ->
                LazyColumn(
                    modifier = Modifier
                        .background(White)
                        .padding(horizontal = dimensions.horizontalMedium).padding(bottom = dimensions.verticalXXSmall),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium)
                ) {
                    items(petsData) { pet ->
                        PetCard(
                            modifier = Modifier.fillMaxWidth(),
                            petData = pet,
                            onEditClick = {
                                onEditClick(pet)
                            },
                            onDeleteClick = { petToDelete = pet }
                        )
                    }
                }
            }
        }
    }
}
