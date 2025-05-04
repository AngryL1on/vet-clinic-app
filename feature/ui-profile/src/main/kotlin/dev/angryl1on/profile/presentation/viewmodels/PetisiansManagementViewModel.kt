package dev.angryl1on.profile.presentation.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dev.angryl1on.vetclinic.common.presentation.ModelIntent
import dev.angryl1on.vetclinic.common.presentation.Reducer
import dev.angryl1on.vetclinic.common.presentation.UiState
import dev.angryl1on.vetclinic.common.presentation.ViewModel
import dev.angryl1on.vetclinic.domain.usecase.petservice.DeletePetUseCase
import dev.angryl1on.vetclinic.domain.usecase.petservice.GetAllPetsUseCase
import dev.angryl1on.vetclinic.domain.usecase.petservice.ObservePetsUseCase
import dev.angryl1on.vetclinic.model.ErrorResponse
import dev.angryl1on.vetclinic.model.pet.PetResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PetisiansManagementViewModel(
    private val getAllPetsUseCase: GetAllPetsUseCase,
    private val observePetsUseCase: ObservePetsUseCase,
    private val deletePetUseCase: DeletePetUseCase
) : ViewModel<PetisiansManagementScreenState, PetisiansManagementScreenIntent>() {

    private val reducer = PetisiansManagementScreenReducer(PetisiansManagementScreenState.Init)

    override val state: Flow<PetisiansManagementScreenState>
        get() = reducer.state

    private val _petsDataState: MutableStateFlow<List<PetResponse>> = MutableStateFlow(emptyList())
    val petsDataState: StateFlow<List<PetResponse>> = _petsDataState.asStateFlow()

    init {
        viewModelScope.launch {
            observePetsUseCase()
                .onEach { list ->
                    _petsDataState.value = list
                    reducer.sendIntent(PetisiansManagementScreenIntent.LoadSuccess(list))
                }
                .catch { e ->
                    reducer.sendIntent(
                        PetisiansManagementScreenIntent.LoadFailure(
                            ErrorResponse(-1, e.message)
                        )
                    )
                }
                .launchIn(this)
        }

        // 2) Делаем первый «синхрон»
        refresh()
    }

    /** «Обновить с сервера» (Pull-to-refresh и первый вызов) */
    private fun refresh() = viewModelScope.launch {
        reducer.sendIntent(PetisiansManagementScreenIntent.Load)
        getAllPetsUseCase()
            .onFailure { e ->
                if (_petsDataState.value.isEmpty()) {
                    reducer.sendIntent(
                        PetisiansManagementScreenIntent.LoadFailure(
                            ErrorResponse(-1, e.message)
                        )
                    )
                }
            }
    }

    fun deletePet(id: Long) = viewModelScope.launch {
        reducer.sendIntent(PetisiansManagementScreenIntent.DeletePet(id))
        deletePetUseCase(id)
            .onSuccess {
                // ViewModel не трогает Room напрямую,
                // но можно сразу отфильтровать UI-список:
                _petsDataState.value = _petsDataState.value.filter { it.id != id }
                reducer.sendIntent(PetisiansManagementScreenIntent.DeleteSuccess(id))
            }
            .onFailure { e ->
                reducer.sendIntent(
                    PetisiansManagementScreenIntent.DeleteFailure(
                        ErrorResponse(-1, e.message)
                    )
                )
            }
    }

    private class PetisiansManagementScreenReducer(initial: PetisiansManagementScreenState.Init) :
        Reducer<PetisiansManagementScreenState, PetisiansManagementScreenIntent>(initial) {
        override fun reduce(
            oldState: PetisiansManagementScreenState,
            intent: PetisiansManagementScreenIntent
        ) {
            when (intent) {
                is PetisiansManagementScreenIntent.Load -> {
                    setState(PetisiansManagementScreenState.Loading)
                }

                is PetisiansManagementScreenIntent.LoadSuccess -> {
                    setState(PetisiansManagementScreenState.Success)
                }

                is PetisiansManagementScreenIntent.LoadFailure -> {
                    setState(PetisiansManagementScreenState.Error(message = null))
                }

                is PetisiansManagementScreenIntent.DeletePet -> {
                    setState(PetisiansManagementScreenState.Loading)
                }

                is PetisiansManagementScreenIntent.DeleteSuccess -> {
                    setState(PetisiansManagementScreenState.Success)
                }

                is PetisiansManagementScreenIntent.DeleteFailure -> {
                    setState(PetisiansManagementScreenState.Error(message = intent.error.message))
                }
            }
        }
    }
}

@Immutable
sealed class PetisiansManagementScreenIntent : ModelIntent {
    data object Load : PetisiansManagementScreenIntent()
    data class LoadSuccess(val model: List<PetResponse>) : PetisiansManagementScreenIntent()
    data class LoadFailure(val error: ErrorResponse) : PetisiansManagementScreenIntent()
    data class DeletePet(val petId: Long) : PetisiansManagementScreenIntent()
    data class DeleteSuccess(val petId: Long) : PetisiansManagementScreenIntent()
    data class DeleteFailure(val error: ErrorResponse) : PetisiansManagementScreenIntent()
}

@Immutable
sealed class PetisiansManagementScreenState : UiState {
    data object Init : PetisiansManagementScreenState()
    data object Loading : PetisiansManagementScreenState()
    data object Success : PetisiansManagementScreenState()
    data class Error(val message: String?) : PetisiansManagementScreenState()
}
