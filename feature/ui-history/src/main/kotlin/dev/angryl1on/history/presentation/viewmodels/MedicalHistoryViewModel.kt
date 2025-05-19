package dev.angryl1on.history.presentation.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dev.angryl1on.vetclinic.common.presentation.ModelIntent
import dev.angryl1on.vetclinic.common.presentation.Reducer
import dev.angryl1on.vetclinic.common.presentation.UiState
import dev.angryl1on.vetclinic.common.presentation.ViewModel
import dev.angryl1on.vetclinic.domain.usecase.medicalrecordservice.GetMedicalRecordByPetIdUseCase
import dev.angryl1on.vetclinic.model.ErrorResponse
import dev.angryl1on.vetclinic.model.medicalrecord.MedicalRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MedicalHistoryViewModel(
    private val petId: Long,
    private val getMedicalRecordByPetId: GetMedicalRecordByPetIdUseCase
) : ViewModel<MedicalHistoryState, MedicalHistoryIntent>() {

    private val reducer = MedicalHistoryReducer(MedicalHistoryState.Init)
    override val state: StateFlow<MedicalHistoryState> = reducer.state

    private val _records = MutableStateFlow<List<MedicalRecord>>(emptyList())
    val records: StateFlow<List<MedicalRecord>> = _records.asStateFlow()

    init {
        refresh()
    }

    fun refresh() = viewModelScope.launch {
        reducer.sendIntent(MedicalHistoryIntent.Load)
        getMedicalRecordByPetId(petId)
            .onSuccess { list ->
                _records.value = list
                reducer.sendIntent(MedicalHistoryIntent.LoadSuccess)
            }
            .onFailure { e ->
                reducer.sendIntent(
                    MedicalHistoryIntent.LoadFailure(
                        ErrorResponse(-1, e.message)
                    )
                )
            }
    }

    private class MedicalHistoryReducer(initial: MedicalHistoryState) :
        Reducer<MedicalHistoryState, MedicalHistoryIntent>(initial) {
        override fun reduce(oldState: MedicalHistoryState, intent: MedicalHistoryIntent) {
            when (intent) {
                MedicalHistoryIntent.Load -> setState(MedicalHistoryState.Loading)
                MedicalHistoryIntent.LoadSuccess -> setState(MedicalHistoryState.Success)
                is MedicalHistoryIntent.LoadFailure -> setState(
                    MedicalHistoryState.Error(intent.error.message)
                )
            }
        }
    }
}

@Immutable
sealed class MedicalHistoryIntent : ModelIntent {
    data object Load : MedicalHistoryIntent()
    data object LoadSuccess : MedicalHistoryIntent()
    data class LoadFailure(val error: ErrorResponse) : MedicalHistoryIntent()
}

@Immutable
sealed class MedicalHistoryState : UiState {
    data object Init : MedicalHistoryState()
    data object Loading : MedicalHistoryState()
    data object Success : MedicalHistoryState()
    data class Error(val message: String?) : MedicalHistoryState()
}
