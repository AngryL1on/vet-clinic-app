package dev.angryl1on.history.presentation.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dev.angryl1on.vetclinic.common.presentation.ModelIntent
import dev.angryl1on.vetclinic.common.presentation.Reducer
import dev.angryl1on.vetclinic.common.presentation.UiState
import dev.angryl1on.vetclinic.common.presentation.ViewModel
import dev.angryl1on.vetclinic.domain.usecase.petservice.GetAllPetsUseCase
import dev.angryl1on.vetclinic.model.ErrorResponse
import dev.angryl1on.vetclinic.model.pet.PetResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryScreenViewModel(
    private val getAllPetsUseCase: GetAllPetsUseCase
) : ViewModel<HistoryScreenState, HistoryScreenIntent>() {

    private val reducer = HistoryScreenReducer(HistoryScreenState.Init)

    override val state: StateFlow<HistoryScreenState>
        get() = reducer.state

    private val _petsDataState = MutableStateFlow<List<PetResponse>>(emptyList())
    val petsDataState: StateFlow<List<PetResponse>> = _petsDataState.asStateFlow()

    init {
        // Первый запуск загрузки
        refresh()
    }

    /** Метод для загрузки списка питомцев */
    fun refresh() = viewModelScope.launch {
        reducer.sendIntent(HistoryScreenIntent.Load)
        getAllPetsUseCase()
            .onSuccess { list ->
                _petsDataState.value = list
                reducer.sendIntent(HistoryScreenIntent.LoadSuccess(list))
            }
            .onFailure { e ->
                // Если UI-перечень пока пуст, показываем ошибку
                if (_petsDataState.value.isEmpty()) {
                    reducer.sendIntent(
                        HistoryScreenIntent.LoadFailure(
                            ErrorResponse(statusCode = -1, message = e.message)
                        )
                    )
                }
            }
    }

    private class HistoryScreenReducer(
        initial: HistoryScreenState.Init
    ) : Reducer<HistoryScreenState, HistoryScreenIntent>(initial) {
        override fun reduce(
            oldState: HistoryScreenState,
            intent: HistoryScreenIntent
        ) {
            when (intent) {
                HistoryScreenIntent.Load -> {
                    setState(HistoryScreenState.Loading)
                }

                is HistoryScreenIntent.LoadSuccess -> {
                    setState(HistoryScreenState.Success)
                }

                is HistoryScreenIntent.LoadFailure -> {
                    setState(HistoryScreenState.Error(message = intent.error.message))
                }
            }
        }
    }
}

@Immutable
sealed class HistoryScreenIntent : ModelIntent {
    data object Load : HistoryScreenIntent()
    data class LoadSuccess(val pets: List<PetResponse>) : HistoryScreenIntent()
    data class LoadFailure(val error: ErrorResponse) : HistoryScreenIntent()
}

@Immutable
sealed class HistoryScreenState : UiState {
    data object Init : HistoryScreenState()
    data object Loading : HistoryScreenState()
    data object Success : HistoryScreenState()
    data class Error(val message: String?) : HistoryScreenState()
}
