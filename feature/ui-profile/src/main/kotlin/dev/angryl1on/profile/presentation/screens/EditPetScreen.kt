package dev.angryl1on.profile.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import dev.angryl1on.profile.R
import dev.angryl1on.profile.presentation.viewmodels.EditPetScreenState
import dev.angryl1on.profile.presentation.viewmodels.EditPetViewModel
import dev.angryl1on.vetclinic.model.pet.PetResponse
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton
import dev.angryl1on.vetclinic.ui.components.dialogs.CustomAlertDialog
import dev.angryl1on.vetclinic.ui.components.fields.PrimaryTextField
import dev.angryl1on.vetclinic.ui.components.pickers.DatePickerTextField
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.White
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EditPetScreen(
    initialPet: PetResponse,
    viewModel: EditPetViewModel = koinViewModel(parameters = { parametersOf(initialPet) }),
    onBack: () -> Unit
) {
    var showSuccessDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.state.collectAsState(initial = EditPetScreenState.Init)

    val name by viewModel.name.collectAsState()
    val animalType by viewModel.animalType.collectAsState()
    val breed by viewModel.breed.collectAsState()

    val nameError by viewModel.nameError.collectAsState()
    val animalTypeError by viewModel.animalTypeError.collectAsState()
    val breedError by viewModel.breedError.collectAsState()
    val birthDateError by viewModel.birthDateError.collectAsState()

    val dimensions = LocalDimensions.current

//    val context = LocalContext.current
//    val photoUri by viewModel.photoUri.collectAsState()
//    val photoPicker = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri -> viewModel.onPhotoSelected(uri) }

    val initialDateMillis: Long? = remember(initialPet.birthDate) {
        runCatching {
            SimpleDateFormat("yyyy-MM-dd", Locale.US)
                .parse(initialPet.birthDate)
                ?.time
        }.getOrNull()
    }

    LaunchedEffect(uiState) {
        if (uiState is EditPetScreenState.Success) {
            showSuccessDialog = true
        }
    }

    if (showSuccessDialog) {
        CustomAlertDialog(
            titleText = stringResource(R.string.petetz_data_updated), // например "Успех"
            bodyText = stringResource(R.string.petetz_data_updated_text), // например "Данные питомца сохранены"
            confirmText = stringResource(R.string.done), // "Ок"
            dismissText = null, // если нужно только одна кнопка, можно null
            onDismissRequest = {
                showSuccessDialog = false
                onBack()
            },
            onConfirmButtonClick = {
                showSuccessDialog = false
                onBack()
            },
            onDismissButtonClick = { /* Do nothing */ }
        )
    }

    BackHandler(onBack = onBack)

    Surface(
        color = White,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = dimensions.horizontalMedium,
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                title = stringResource(R.string.pet_name),
                placeholder = stringResource(R.string.enter_name_pet),
                isEnabled = uiState !is EditPetScreenState.Loading,
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
                isEnabled = uiState !is EditPetScreenState.Loading,
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
                initialDate = initialDateMillis,
                onDateSelected = { millis ->
                    millis?.let {
                        // при выборе мы по-прежнему кладём в VM строку  dd.MM.yyyy
                        val formatted = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                            .format(Date(it))
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
                isEnabled = uiState !is EditPetScreenState.Loading,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                onTextChange = viewModel::onBreedChanged
            )

            Spacer(Modifier.height(dimensions.verticalMedium))

//            OutlinePrimaryButton(
//                text = "Выбрать фото",
//                leftIcon = dev.angryl1on.vetclinic.ui.R.drawable.ic_upload_data,
//                onButtonClick = { photoPicker.launch("image/*") },
//                isEnabled = uiState !is EditPetScreenState.Loading
//            )
//
//            photoUri?.let {
//                Spacer(Modifier.height(8.dp))
//                AsyncImage(
//                    model = it,
//                    contentDescription = "Фото питомца",
//                    modifier = Modifier
//                        .size(80.dp)
//                        .clip(CircleShape)
//                        .border(1.dp, Color.Gray, CircleShape),
//                    contentScale = ContentScale.Crop
//                )
//
//                Spacer(Modifier.height(8.dp))
//
//                // Кнопка «Загрузить на сервер»
//                OutlinePrimaryButton(
//                    text = "Обновить фото",
//                    leftIcon = dev.angryl1on.vetclinic.ui.R.drawable.ic_upload_data,
//                    onButtonClick = { viewModel.uploadPhoto(context) },
//                    isEnabled = uiState !is EditPetScreenState.Loading
//                )
//            }

            PrimaryButton(
                text = if (uiState is EditPetScreenState.Loading) stringResource(R.string.safekeeping) else stringResource(
                    R.string.update_data
                ),
                onButtonClick = viewModel::submit,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                isEnabled = uiState !is EditPetScreenState.Loading
            )
        }
    }
}
