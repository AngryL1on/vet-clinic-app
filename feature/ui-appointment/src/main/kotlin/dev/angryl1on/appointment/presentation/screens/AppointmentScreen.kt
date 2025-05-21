package dev.angryl1on.appointment.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import dev.angryl1on.appointment.R
import dev.angryl1on.appointment.model.AppointmentStep
import dev.angryl1on.appointment.presentation.viewmodels.AppointmentScreenState
import dev.angryl1on.appointment.presentation.viewmodels.AppointmentViewModel
import dev.angryl1on.vetclinic.common.utils.formatDateForCIS
import dev.angryl1on.vetclinic.common.utils.formatTimeShort
import dev.angryl1on.vetclinic.domain.navigation.Route
import dev.angryl1on.vetclinic.model.appointment.ServiceType
import dev.angryl1on.vetclinic.model.branches.BranchResponse
import dev.angryl1on.vetclinic.model.user.DoctorResponseForSelectInAppointment
import dev.angryl1on.vetclinic.ui.components.buttons.OutlinePrimaryButton
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton
import dev.angryl1on.vetclinic.ui.components.dialogs.CustomAlertDialog
import dev.angryl1on.vetclinic.ui.components.fields.PrimaryDropdownField
import dev.angryl1on.vetclinic.ui.components.pagers.TabRowPager
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppointmentScreen(
    navController: NavController,
    viewModel: AppointmentViewModel = koinViewModel(),
) {
    var currentStep by remember { mutableStateOf(AppointmentStep.PET_AND_SERVICE) }
    val screenState by viewModel.state.collectAsState(initial = AppointmentScreenState.Init())

    val initState = screenState as? AppointmentScreenState.Init ?: return
    val dimensions = LocalDimensions.current

    val onSubmitClick = { viewModel.submitAppointment() }

    LaunchedEffect(Unit) {
        viewModel.refreshPets()
    }

    if (initState.isSubmitted) {
        CustomAlertDialog(
            titleText = "Запись создана",
            bodyText = "Вы успешно записались на прием",
            confirmText = "Ок",
            dismissText = "",
            onDismissRequest = {},
            onConfirmButtonClick = {
                navController.navigate(Route.MainScreen) {
                    popUpTo(Route.AppointmentScreen) { inclusive = true }
                }
            },
            onDismissButtonClick = {}
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensions.horizontalMedium, vertical = dimensions.verticalXSmall),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium)
    ) {

        LinearProgressIndicator(
            progress = {
                when (currentStep) {
                    AppointmentStep.PET_AND_SERVICE -> 1 / 3f
                    AppointmentStep.BRANCH_AND_DOCTOR -> 2 / 3f
                    AppointmentStep.DATE_AND_TIME -> 1f
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )

        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
            },
            label = "StepTransition"
        ) { step ->
            when (step) {
                AppointmentStep.PET_AND_SERVICE -> {
                    StepPetAndService(
                        state = initState,
                        onPetSelected = viewModel::selectPet,
                        onServiceSelected = { displayName ->
                            ServiceType.fromDisplayName(displayName)?.let { viewModel.selectService(it) }
                        },
                        onNext = {
                            if (viewModel.canGoToStep2()) {
                                currentStep = AppointmentStep.BRANCH_AND_DOCTOR
                            }
                        }
                    )
                }

                AppointmentStep.BRANCH_AND_DOCTOR -> {
                    StepBranchAndDoctor(
                        branches = initState.branches,
                        doctors = initState.doctors,
                        selectedBranch = initState.selectedBranch,
                        selectedDoctorId = initState.selectedDoctorId,
                        onBranchSelected = viewModel::selectBranch,
                        onDoctorSelected = viewModel::selectDoctor,
                        onNext = {
                            if (viewModel.canGoToStep3()) {
                                currentStep = AppointmentStep.DATE_AND_TIME
                            }
                        },
                        onBack = {
                            viewModel.resetStep2()
                            currentStep = AppointmentStep.PET_AND_SERVICE
                        }
                    )
                }

                AppointmentStep.DATE_AND_TIME -> {
                    StepDateAndTime(
                        scheduleDates = initState.scheduleDates,
                        availableTimes = initState.availableTimes,
                        selectedDate = initState.selectedDate,
                        selectedTime = initState.selectedTime,
                        onDateSelected = viewModel::selectDate,
                        onTimeSelected = viewModel::selectTime,
                        onSubmitClick = {
                            if (viewModel.canSubmit()) onSubmitClick()
                        },
                        onBack = {
                            viewModel.resetStep3()
                            currentStep = AppointmentStep.BRANCH_AND_DOCTOR
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StepPetAndService(
    state: AppointmentScreenState.Init,
    onPetSelected: (Long) -> Unit,
    onServiceSelected: (String) -> Unit,
    onNext: () -> Unit
) {
    val dimensions = LocalDimensions.current

    val selectedPetName = remember(state.pets, state.selectedPetId) {
        state.pets.firstOrNull { it.id == state.selectedPetId }?.name ?: ""
    }
    val selectedServiceName = state.selectedService?.displayName ?: ""

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium)
    ) {
        PrimaryDropdownField(
            title = "Питомец",
            placeholder = "Выберете питомца",
            options = state.pets.map { it.name },
            selectedOption = selectedPetName,
            onOptionSelected = { name ->
                state.pets.find { it.name == name }?.let { pet ->
                    onPetSelected(pet.id)
                }
            }
        )

        PrimaryDropdownField(
            title = "Услуга",
            placeholder = "Выберете услугу",
            options = ServiceType.entries.map { it.displayName },
            selectedOption = selectedServiceName,
            onOptionSelected = onServiceSelected
        )

        PrimaryButton(
            text = stringResource(R.string.next),
            onButtonClick = onNext,
            modifier = Modifier.wrapContentWidth()
        )
    }
}

@Composable
fun StepBranchAndDoctor(
    branches: List<BranchResponse>,
    doctors: List<DoctorResponseForSelectInAppointment>,
    selectedBranch: String,
    selectedDoctorId: Long?,
    onBranchSelected: (String) -> Unit,
    onDoctorSelected: (Long) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val dimensions = LocalDimensions.current

    val branchNames = branches.map { it.shortName }
    val doctorNames = doctors.map { it.fullName }
    val selectedDoctorName = doctors.firstOrNull { it.id == selectedDoctorId }?.fullName ?: ""

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium)
    )  {
        TabRowPager(
            modifier = Modifier.wrapContentHeight(),
            tabs = listOf(
                "Список" to {
                    PrimaryDropdownField(
                        title = "Филиал",
                        placeholder = "Выберете филиал",
                        options = branchNames,
                        selectedOption = selectedBranch,
                        onOptionSelected = onBranchSelected
                    )
                },
                "Карта" to {
                    Text(text = "Здесь планируется карта, но она в разработке, потому тут пока что просто текст)")
//                    BranchMapView(
//                        branches = branches,
//                        onBranchSelected = onBranchSelected
//                    )
                }
            ),
            spaceBetweenTabAndPager = dimensions.verticalXSmall
        )

        PrimaryDropdownField(
            title = "Врач",
            options = doctorNames,
            placeholder = "Выберете врача",
            selectedOption = selectedDoctorName,
            isEnabled = selectedBranch.isNotEmpty(),
            onOptionSelected = { name ->
                doctors.find { it.fullName == name }?.let {
                    onDoctorSelected(it.id)
                }
            }
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.wrapContentWidth()
        ) {
            OutlinePrimaryButton(
                text = stringResource(R.string.back),
                modifier = Modifier.wrapContentWidth(),
                onButtonClick = onBack
            )

            Spacer(modifier = Modifier.width(dimensions.horizontalXSmall))

            PrimaryButton(
                text = stringResource(R.string.next),
                onButtonClick = onNext,
                modifier = Modifier.wrapContentWidth()
            )
        }
    }
}

@Composable
fun StepDateAndTime(
    scheduleDates: List<String>,
    selectedDate: String,
    availableTimes: List<String>,
    selectedTime: String,
    onDateSelected: (String) -> Unit,
    onTimeSelected: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onBack: () -> Unit
) {
    val dimensions = LocalDimensions.current

    val formattedDates = remember(scheduleDates) {
        scheduleDates.map { date -> formatDateForCIS(date) to date }
    }

    val formattedTimes = remember(availableTimes) {
        availableTimes.map { formatTimeShort(it) to it }
    }

    val selectedFormattedDate = formattedDates.find { it.second == selectedDate }?.first ?: ""
    val selectedFormattedTime = formattedTimes.find { it.second == selectedTime }?.first ?: ""

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium)
    ) {
        PrimaryDropdownField(
            title = "Дата",
            placeholder = "Выберете дату",
            options = formattedDates.map { it.first },
            selectedOption = selectedFormattedDate,
            onOptionSelected = { selectedDisplay ->
                formattedDates.find { it.first == selectedDisplay }?.second?.let { original ->
                    onDateSelected(original)
                }
            }
        )

        PrimaryDropdownField(
            title = "Время",
            placeholder = "Выберете время",
            options = formattedTimes.map { it.first },
            selectedOption = selectedFormattedTime,
            isEnabled = formattedTimes.isNotEmpty(),
            onOptionSelected = { display ->
                formattedTimes.find { it.first == display }?.second?.let(onTimeSelected)
            }
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.wrapContentWidth()
        ) {
            OutlinePrimaryButton(
                text = stringResource(R.string.back),
                modifier = Modifier.wrapContentWidth(),
                onButtonClick = onBack
            )

            Spacer(modifier = Modifier.width(dimensions.horizontalXSmall))

            PrimaryButton(
                text = "Подтвердить запись",
                onButtonClick = onSubmitClick,
                modifier = Modifier.wrapContentWidth()
            )
        }
    }
}
