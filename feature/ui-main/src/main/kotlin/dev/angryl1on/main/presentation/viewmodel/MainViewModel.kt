package dev.angryl1on.main.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dev.angryl1on.vetclinic.common.presentation.ModelIntent
import dev.angryl1on.vetclinic.common.presentation.Reducer
import dev.angryl1on.vetclinic.common.presentation.UiState
import dev.angryl1on.vetclinic.common.presentation.ViewModel
import dev.angryl1on.vetclinic.domain.usecase.appointmentsservice.CancelAppointmentUseCase
import dev.angryl1on.vetclinic.domain.usecase.appointmentsservice.GetAppointmentByScheduled
import dev.angryl1on.vetclinic.model.ErrorResponse
import dev.angryl1on.vetclinic.model.appointment.AppointmentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val getAppointmentByScheduled: GetAppointmentByScheduled,
    private val cancelAppointmentUseCase: CancelAppointmentUseCase
) : ViewModel<MainScreenState, MainScreenIntent>() {

    /**
     * MVI-infrastructure
     */
    private val reducer = MainScreenReducer(MainScreenState.Init)
    override val state: Flow<MainScreenState>
        get() = reducer.state

    private val _appointments = MutableStateFlow<List<AppointmentResponse>>(emptyList())
    val appointments: StateFlow<List<AppointmentResponse>> = _appointments.asStateFlow()

    init {
        refresh()
    }

    fun refresh() = viewModelScope.launch {
        reducer.sendIntent(MainScreenIntent.Load)
        getAppointmentByScheduled()
            .onSuccess {
                _appointments.value = it
                reducer.sendIntent(MainScreenIntent.LoadSuccess(it))
            }
            .onFailure {
                reducer.sendIntent(
                    MainScreenIntent.LoadFailure(
                        ErrorResponse(-1, it.message ?: "Unknown error")
                    )
                )
            }
    }

    fun cancelAppointment(id: Long) = viewModelScope.launch {
        reducer.sendIntent(MainScreenIntent.CancelAppointment(id))

        cancelAppointmentUseCase(id)
            .onSuccess {
                _appointments.update { list -> list.filterNot { it.id == id } }
            }
            .onFailure { e ->
                val message = e.message ?: "Не удалось отменить приём"
                Timber.e("MainViewModel", "Ошибка отмены приёма", e)
                reducer.sendIntent(MainScreenIntent.CancelFailure(message))
            }
    }

    /**
     * Reducer
     */
    private class MainScreenReducer(initial: MainScreenState) :
        Reducer<MainScreenState, MainScreenIntent>(initial) {

        override fun reduce(oldState: MainScreenState, intent: MainScreenIntent) {
            when (intent) {
                is MainScreenIntent.Load -> setState(MainScreenState.Loading)
                is MainScreenIntent.LoadSuccess -> setState(MainScreenState.Success)
                is MainScreenIntent.LoadFailure -> setState(MainScreenState.Error(intent.error.message))
                is MainScreenIntent.CancelAppointment -> {}
                is MainScreenIntent.CancelFailure -> {}
            }
        }
    }
}

/**
 * INTENTS
 */
@Immutable
sealed class MainScreenIntent : ModelIntent {
    data object Load : MainScreenIntent()
    data class LoadSuccess(val list: List<AppointmentResponse>) : MainScreenIntent()
    data class LoadFailure(val error: ErrorResponse) : MainScreenIntent()
    data class CancelAppointment(val appointmentId: Long) : MainScreenIntent()
    data class CancelFailure(val message: String) : MainScreenIntent()
}

/**
 * STATE
 */
@Immutable
sealed class MainScreenState : UiState {
    data object Init : MainScreenState()
    data object Loading : MainScreenState()
    data object Success : MainScreenState()
    data class Error(val error: String?) : MainScreenState()
}
