package dev.angryl1on.profile.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.angryl1on.profile.R
import dev.angryl1on.profile.presentation.viewmodels.AddPetScreenState
import dev.angryl1on.profile.presentation.viewmodels.AddPetViewModel
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton
import dev.angryl1on.vetclinic.ui.components.fields.PrimaryTextField
import dev.angryl1on.vetclinic.ui.components.pickers.DatePickerTextField
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme
import dev.angryl1on.vetclinic.ui.theme.White
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddPetScreen(
    viewModel: AddPetViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.state.collectAsState(initial = AddPetScreenState.Init)

    val name by viewModel.name.collectAsState()
    val animalType by viewModel.animalType.collectAsState()
    val breed by viewModel.breed.collectAsState()

    val nameError by viewModel.nameError.collectAsState()
    val animalTypeError by viewModel.animalTypeError.collectAsState()
    val breedError by viewModel.breedError.collectAsState()
    val birthDateError by viewModel.birthDateError.collectAsState()

    val dimensions = LocalDimensions.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (uiState) {
            is AddPetScreenState.Error ->
                snackbarHostState.showSnackbar(
                    (uiState as AddPetScreenState.Error).message
                )

            is AddPetScreenState.Success -> onBack()
            else -> Unit
        }
    }

    BackHandler(onBack = onBack)

    Surface(
        color = White,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensions.horizontalMedium)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                title = stringResource(R.string.pet_name),
                placeholder = stringResource(R.string.enter_name_pet),
                isEnabled = uiState !is AddPetScreenState.Loading,
                isError = nameError != null,
                errorText = nameError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                onTextChange = viewModel::onNameChanged
            )

            Spacer(Modifier.height(dimensions.verticalXSmall))

            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = animalType,
                title = stringResource(R.string.type_pet),
                placeholder = stringResource(R.string.enter_type_pet),
                supportText = stringResource(R.string.type_pet_support_text),
                isError = animalTypeError != null,
                errorText = animalTypeError,
                isEnabled = uiState !is AddPetScreenState.Loading,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                onTextChange = viewModel::onAnimalTypeChanged
            )

            Spacer(Modifier.height(dimensions.verticalXSmall))

            DatePickerTextField(
                title = stringResource(R.string.date_of_birth),
                isError = birthDateError != null,
                errorText = birthDateError,
                onDateSelected = { millis ->
                    millis?.let { date ->
                        val formatted = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                            .format(Date(date))

                        viewModel.onBirthDateChanged(formatted)
                    }
                }
            )

            Spacer(Modifier.height(dimensions.verticalXSmall))

            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = breed,
                title = stringResource(R.string.breed),
                placeholder = stringResource(R.string.enter_breed),
                supportText = stringResource(R.string.breed_support_text),
                isError = breedError != null,
                errorText = breedError,
                isEnabled = uiState !is AddPetScreenState.Loading,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                onTextChange = viewModel::onBreedChanged
            )

            Spacer(Modifier.height(dimensions.verticalMedium))

            PrimaryButton(
                text = if (uiState is AddPetScreenState.Loading) stringResource(R.string.safekeeping) else stringResource(
                    R.string.done
                ),
                onButtonClick = viewModel::submit,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                isEnabled = uiState !is AddPetScreenState.Loading
            )
        }
    }
}

@Composable
@Preview
fun AddPetScreenPreview() {
    VetClinicTheme {
        Surface {
            AddPetScreen(
                onBack = { /* do nothing */ }
            )
        }
    }
}
