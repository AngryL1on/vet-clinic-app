package dev.angryl1on.appointment.presentation.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dev.angryl1on.vetclinic.common.presentation.ModelIntent
import dev.angryl1on.vetclinic.common.presentation.Reducer
import dev.angryl1on.vetclinic.common.presentation.UiState
import dev.angryl1on.vetclinic.common.presentation.ViewModel
import dev.angryl1on.vetclinic.domain.usecase.appointmentsservice.CreateAppointmentUseCase
import dev.angryl1on.vetclinic.domain.usecase.appointmentsservice.GetAvailableSlotsUseCase
import dev.angryl1on.vetclinic.domain.usecase.branchesservice.GetBranchByServiceNameUseCase
import dev.angryl1on.vetclinic.domain.usecase.petservice.GetAllPetsUseCase
import dev.angryl1on.vetclinic.domain.usecase.schedulesservice.GetScheduleByDoctorIdUseCase
import dev.angryl1on.vetclinic.domain.usecase.userservice.GetDoctorsByBranchIdUseCase
import dev.angryl1on.vetclinic.model.appointment.AppointmentRequest
import dev.angryl1on.vetclinic.model.appointment.ServiceType
import dev.angryl1on.vetclinic.model.branches.BranchResponse
import dev.angryl1on.vetclinic.model.pet.PetResponse
import dev.angryl1on.vetclinic.model.user.DoctorResponseForSelectInAppointment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

class AppointmentViewModel(
    private val getAllPetsUseCase: GetAllPetsUseCase,
    private val getBranchByServiceNameUseCase: GetBranchByServiceNameUseCase,
    private val getDoctorsByBranchIdUseCase: GetDoctorsByBranchIdUseCase,
    private val getScheduleByDoctorIdUseCase: GetScheduleByDoctorIdUseCase,
    private val getAvailableSlotsUseCase: GetAvailableSlotsUseCase,
    private val createAppointmentUseCase: CreateAppointmentUseCase
) : ViewModel<AppointmentScreenState, AppointmentScreenIntent>() {

    /* MVI‑infrastructure */
    private val reducer = AppointmentScreenReducer(AppointmentScreenState.Init())
    override val state: Flow<AppointmentScreenState>
        get() = reducer.state

    init {
        loadPets()
    }

    private fun loadPets() {
        reducer.sendIntent(AppointmentScreenIntent.LoadPets)

        viewModelScope.launch {
            getAllPetsUseCase()
                .onSuccess { pets ->
                    reducer.sendIntent(AppointmentScreenIntent.PetsLoaded(pets))
                }
                .onFailure { throwable ->
                    reducer.sendIntent(AppointmentScreenIntent.PetsLoadFailed(throwable.message.orEmpty()))
                }
        }
    }

    fun refreshPets() {
        viewModelScope.launch {
            getAllPetsUseCase()
                .onSuccess { pets ->
                    reducer.sendIntent(AppointmentScreenIntent.PetsLoaded(pets))
                }
                .onFailure { throwable ->
                    reducer.sendIntent(AppointmentScreenIntent.PetsLoadFailed(throwable.message.orEmpty()))
                }
        }
    }

    fun selectPet(petId: Long) {
        reducer.sendIntent(AppointmentScreenIntent.PetSelected(petId))
    }

    fun selectService(service: ServiceType) {
        reducer.sendIntent(AppointmentScreenIntent.ServiceSelected(service))

        viewModelScope.launch {
            Timber.tag("Запрос филиалов по услуге: ${service.name}")
            getBranchByServiceNameUseCase(service.name)
                .onSuccess { branches ->
                    Timber.tag("Филиалы получены: ${branches.map { it.shortName }}")
                    reducer.sendIntent(AppointmentScreenIntent.BranchesLoaded(branches))
                }
                .onFailure { throwable ->
                    Timber.tag("Ошибка загрузки филиалов: ${throwable.message}")
                    reducer.sendIntent(AppointmentScreenIntent.BranchesLoadFailed(throwable.message.orEmpty()))
                }
        }
    }

    fun selectBranch(branchShortName: String) {
        reducer.sendIntent(AppointmentScreenIntent.BranchSelected(branchShortName))

        val branch = (reducer.state.value as? AppointmentScreenState.Init)
            ?.branches
            ?.firstOrNull { it.shortName == branchShortName } ?: return

        viewModelScope.launch {
            getDoctorsByBranchIdUseCase(branch.id)
                .onSuccess { doctors ->
                    reducer.sendIntent(AppointmentScreenIntent.DoctorsLoaded(doctors))
                }
                .onFailure { throwable ->
                    reducer.sendIntent(AppointmentScreenIntent.DoctorsLoadFailed(throwable.message.orEmpty()))
                }
        }
    }

    fun selectDoctor(doctorId: Long) {
        reducer.sendIntent(AppointmentScreenIntent.DoctorSelected(doctorId))

        viewModelScope.launch {
            getScheduleByDoctorIdUseCase(doctorId)
                .onSuccess { schedule ->
                    val dates = schedule.map { it.date }
                    reducer.sendIntent(AppointmentScreenIntent.ScheduleLoaded(dates))
                }
                .onFailure { throwable ->
                    reducer.sendIntent(AppointmentScreenIntent.ScheduleLoadFailed(throwable.message.orEmpty()))
                }
        }
    }

    fun selectDate(date: String) {
        reducer.sendIntent(AppointmentScreenIntent.DateSelected(date))

        val doctorId = (reducer.state.value as? AppointmentScreenState.Init)?.selectedDoctorId
        val serviceType = (reducer.state.value as? AppointmentScreenState.Init)?.selectedService

        if (doctorId != null && serviceType != null) {
            viewModelScope.launch {
                getAvailableSlotsUseCase(
                    doctorId = doctorId,
                    date = date,
                    type = serviceType.name
                ).onSuccess { times ->
                    reducer.sendIntent(AppointmentScreenIntent.AvailableTimesLoaded(times))
                }.onFailure { throwable ->
                    reducer.sendIntent(AppointmentScreenIntent.AvailableTimesLoadFailed(throwable.message.orEmpty()))
                }
            }
        }
    }

    fun selectTime(time: String) {
        reducer.sendIntent(AppointmentScreenIntent.TimeSelected(time))
    }

    fun submitAppointment() {
        val state = reducer.state.value as? AppointmentScreenState.Init ?: return

        val appointmentRequest = AppointmentRequest(
            doctorId = state.selectedDoctorId ?: return,
            petId = state.selectedPetId ?: return,
            appointmentDate = state.selectedDate.ifBlank { return },
            appointmentStartTime = state.selectedTime.ifBlank { return },
            appointmentType = state.selectedService?.displayName ?: return
        )

        Timber.d("Создание заявки: $appointmentRequest")

        reducer.sendIntent(AppointmentScreenIntent.SubmitStart)

        viewModelScope.launch {
            try {
                val result = createAppointmentUseCase(appointmentRequest)
                Timber.d("Результат создания: $result")

                if (result.getOrNull() == true) {
                    reducer.sendIntent(AppointmentScreenIntent.SubmitSuccess())
                } else {
                    reducer.sendIntent(AppointmentScreenIntent.SubmitFailure("Не удалось создать запись (false)"))
                }
            } catch (e: Exception) {
                Timber.e(e, "Ошибка при создании заявки")
                reducer.sendIntent(
                    AppointmentScreenIntent.SubmitFailure(
                        e.message ?: "Неизвестная ошибка"
                    )
                )
            }
        }
    }

    fun canGoToStep2(): Boolean {
        val state = reducer.state.value as? AppointmentScreenState.Init ?: return false
        return state.selectedPetId != null && state.selectedService != null
    }

    fun canGoToStep3(): Boolean {
        val state = reducer.state.value as? AppointmentScreenState.Init ?: return false
        return state.selectedBranch.isNotBlank() && state.selectedDoctorId != null
    }

    fun canSubmit(): Boolean {
        val state = reducer.state.value as? AppointmentScreenState.Init ?: return false
        return state.selectedDate.isNotBlank() && state.selectedTime.isNotBlank()
    }

    fun resetStep2() {
        reducer.sendIntent(
            AppointmentScreenIntent.BranchSelected(branchShortName = "")
        )
        reducer.sendIntent(
            AppointmentScreenIntent.DoctorsLoaded(emptyList())
        )
        reducer.sendIntent(
            AppointmentScreenIntent.DoctorSelected(doctorId = 0L)
        )
        reducer.sendIntent(
            AppointmentScreenIntent.ScheduleLoaded(emptyList())
        )
    }

    fun resetStep3() {
        reducer.sendIntent(AppointmentScreenIntent.DateSelected(""))
        reducer.sendIntent(AppointmentScreenIntent.TimeSelected(""))
        reducer.sendIntent(AppointmentScreenIntent.AvailableTimesLoaded(emptyList()))
    }

    private class AppointmentScreenReducer(initial: AppointmentScreenState) :
        Reducer<AppointmentScreenState, AppointmentScreenIntent>(initial) {

        override fun reduce(oldState: AppointmentScreenState, intent: AppointmentScreenIntent) {
            if (oldState !is AppointmentScreenState.Init) return

            when (intent) {
                AppointmentScreenIntent.LoadPets -> {
                    setState(oldState.copy(isLoading = true))
                }

                is AppointmentScreenIntent.PetsLoaded -> {
                    setState(oldState.copy(isLoading = false, pets = intent.pets))
                }

                is AppointmentScreenIntent.PetsLoadFailed -> {
                    setState(oldState.copy(isLoading = false, error = intent.error))
                }

                is AppointmentScreenIntent.PetSelected -> {
                    setState(oldState.copy(selectedPetId = intent.petId))
                }

                is AppointmentScreenIntent.BranchesLoaded -> {
                    setState(oldState.copy(branches = intent.branches))
                }

                is AppointmentScreenIntent.BranchesLoadFailed -> {
                    setState(oldState.copy(error = intent.error))
                }

                is AppointmentScreenIntent.ServiceSelected -> {
                    setState(oldState.copy(selectedService = intent.service))
                }

                is AppointmentScreenIntent.BranchSelected -> {
                    setState(
                        oldState.copy(
                            selectedBranch = intent.branchShortName,
                            doctors = emptyList(),
                            selectedDoctorId = null
                        )
                    )
                }

                is AppointmentScreenIntent.DoctorsLoaded -> {
                    setState(oldState.copy(doctors = intent.doctors))
                }

                is AppointmentScreenIntent.DoctorsLoadFailed -> {
                    setState(oldState.copy(error = intent.error))
                }

                is AppointmentScreenIntent.DoctorSelected -> {
                    setState(oldState.copy(selectedDoctorId = intent.doctorId))
                }

                is AppointmentScreenIntent.ScheduleLoaded -> {
                    setState(oldState.copy(scheduleDates = intent.dates))
                }

                is AppointmentScreenIntent.ScheduleLoadFailed -> {
                    setState(oldState.copy(error = intent.error))
                }

                is AppointmentScreenIntent.DateSelected -> {
                    setState(oldState.copy(selectedDate = intent.date))
                }

                is AppointmentScreenIntent.AvailableTimesLoaded -> {
                    setState(oldState.copy(availableTimes = intent.times))
                }

                is AppointmentScreenIntent.AvailableTimesLoadFailed -> {
                    setState(oldState.copy(error = intent.error))
                }

                is AppointmentScreenIntent.TimeSelected -> {
                    setState(oldState.copy(selectedTime = intent.time))
                }

                is AppointmentScreenIntent.SubmitStart -> {
                    setState(oldState.copy(isLoading = true))
                }

                is AppointmentScreenIntent.SubmitSuccess -> {
                    setState(oldState.copy(isLoading = false, isSubmitted = true))
                }

                is AppointmentScreenIntent.SubmitFailure -> {
                    setState(AppointmentScreenState.Error(intent.error))
                }

                is AppointmentScreenIntent.ResetStep2Data -> {
                    setState(
                        oldState.copy(
                            selectedBranch = "",
                            selectedDoctorId = null,
                            doctors = emptyList()
                        )
                    )
                }

                is AppointmentScreenIntent.ResetStep3Data -> {
                    setState(
                        oldState.copy(
                            selectedDate = "",
                            selectedTime = "",
                            availableTimes = emptyList()
                        )
                    )
                }
            }
        }
    }
}

@Immutable
sealed class AppointmentScreenIntent : ModelIntent {
    data object LoadPets : AppointmentScreenIntent()
    data class PetsLoaded(val pets: List<PetResponse>) : AppointmentScreenIntent()
    data class PetsLoadFailed(val error: String) : AppointmentScreenIntent()

    data class BranchesLoaded(val branches: List<BranchResponse>) : AppointmentScreenIntent()
    data class BranchesLoadFailed(val error: String) : AppointmentScreenIntent()

    data class DoctorsLoaded(val doctors: List<DoctorResponseForSelectInAppointment>) :
        AppointmentScreenIntent()

    data class DoctorsLoadFailed(val error: String) : AppointmentScreenIntent()

    data class ScheduleLoaded(val dates: List<String>) : AppointmentScreenIntent()
    data class ScheduleLoadFailed(val error: String) : AppointmentScreenIntent()

    data class AvailableTimesLoaded(val times: List<String>) : AppointmentScreenIntent()
    data class AvailableTimesLoadFailed(val error: String) : AppointmentScreenIntent()

    data class DoctorSelected(val doctorId: Long) : AppointmentScreenIntent()

    data class PetSelected(val petId: Long) : AppointmentScreenIntent()

    data class ServiceSelected(val service: ServiceType) : AppointmentScreenIntent()

    data class BranchSelected(val branchShortName: String) : AppointmentScreenIntent()

    data class DateSelected(val date: String) : AppointmentScreenIntent()

    data class TimeSelected(val time: String) : AppointmentScreenIntent()

    data object ResetStep2Data : AppointmentScreenIntent()
    data object ResetStep3Data : AppointmentScreenIntent()

    data object SubmitStart : AppointmentScreenIntent()
    data class SubmitSuccess(val message: String = "Запись подтверждена") :
        AppointmentScreenIntent()

    data class SubmitFailure(val error: String) : AppointmentScreenIntent()
}

@Immutable
sealed class AppointmentScreenState : UiState {
    data class Init(
        val isLoading: Boolean = false,
        val pets: List<PetResponse> = emptyList(),
        val branches: List<BranchResponse> = emptyList(),
        val doctors: List<DoctorResponseForSelectInAppointment> = emptyList(),
        val scheduleDates: List<String> = emptyList(), // "2025-05-25"
        val availableTimes: List<String> = emptyList(), // "09:45:00"
        val selectedPetId: Long? = null,
        val selectedService: ServiceType? = null,
        val selectedDoctorId: Long? = null,
        val selectedBranch: String = "",
        val selectedDoctor: String = "",
        val selectedDate: String = "",
        val selectedTime: String = "",
        val appointmentTypeDisplayName: String = "",
        val isSubmitted: Boolean = false,
        val error: String? = null
    ) : AppointmentScreenState()

    data class Error(val message: String) : AppointmentScreenState()
}
