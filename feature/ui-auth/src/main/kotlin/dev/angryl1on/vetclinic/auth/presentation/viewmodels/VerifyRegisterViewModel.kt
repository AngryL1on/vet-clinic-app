package dev.angryl1on.vetclinic.auth.presentation.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.angryl1on.vetclinic.common.presentation.ModelIntent
import dev.angryl1on.vetclinic.common.presentation.Reducer
import dev.angryl1on.vetclinic.common.presentation.UiState
import dev.angryl1on.vetclinic.common.presentation.ViewModel
import dev.angryl1on.vetclinic.domain.usecase.authservice.VerifyUseCase
import dev.angryl1on.vetclinic.model.auth.VerifyRegisterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class VerifyRegisterViewModel(
    savedStateHandle: SavedStateHandle,
    private val verifyUseCase: VerifyUseCase
) : ViewModel<VerifyScreenState, VerifyScreenIntent>() {

    private val reducer = VerifyScreenReducer(VerifyScreenState.Init)
    override val state: Flow<VerifyScreenState>
        get() = reducer.state

    private val originalEmail: String = savedStateHandle["email"] ?: ""

    private val _code = MutableStateFlow("")
    private val _codeErr = MutableStateFlow<String?>(null)

    val code: StateFlow<String> get() = _code
    val codeErr: StateFlow<String?> get() = _codeErr

    fun onCodeChanged(v: String) = dispatch(VerifyScreenIntent.CodeChanged(v))
    fun submit() = dispatch(VerifyScreenIntent.Submit)

    private fun dispatch(intent: VerifyScreenIntent) {
        when (intent) {
            is VerifyScreenIntent.CodeChanged -> {
                _code.value = intent.value
                _codeErr.value = validateCode(intent.value)
            }

            VerifyScreenIntent.Submit -> processSubmit()

            else -> {}
        }
        reducer.sendIntent(intent)
    }

    private fun processSubmit() {
        viewModelScope.launch {
            if (_code.value.isBlank()) {
                _codeErr.value = "Введите код из письма"
                reducer.sendIntent(
                    VerifyScreenIntent.SubmitFailure("Код не может быть пустым")
                )
                return@launch
            }

            reducer.sendIntent(VerifyScreenIntent.SubmitStart)

            val body = VerifyRegisterData(
                email = originalEmail,
                verificationCode = _code.value.trim()
            )

            verifyUseCase(model = body)
                .onSuccess {
                    reducer.sendIntent(VerifyScreenIntent.VerifySuccess)
                    Timber.d("Verify success for $originalEmail")
                }
                .onFailure { t ->
                    reducer.sendIntent(VerifyScreenIntent.SubmitFailure(t.message.toString()))
                }
        }
    }

    private fun validateCode(v: String) = when {
        v.isBlank() -> "Введите код"
        v.length != 6 -> "Код должен содержать 6 символов"
        else -> null
    }

    /* Reducer ------------------------------------------------------------ */
    private class VerifyScreenReducer(initial: VerifyScreenState) :
        Reducer<VerifyScreenState, VerifyScreenIntent>(initial) {

        override fun reduce(oldState: VerifyScreenState, intent: VerifyScreenIntent) {
            when (intent) {
                VerifyScreenIntent.SubmitStart ->
                    setState(VerifyScreenState.Loading)

                VerifyScreenIntent.VerifySuccess ->
                    setState(VerifyScreenState.Success) // UI → navigate to StartScreen

                is VerifyScreenIntent.SubmitFailure ->
                    setState(VerifyScreenState.Error(intent.error))

                else -> Unit
            }
        }
    }
}

/* INTENTS --------------------------------------------------------------- */
@Immutable
sealed class VerifyScreenIntent : ModelIntent {
    data class CodeChanged(val value: String) : VerifyScreenIntent()
    data object Submit : VerifyScreenIntent()
    data object SubmitStart : VerifyScreenIntent()
    data object VerifySuccess : VerifyScreenIntent()
    data class SubmitFailure(val error: String) : VerifyScreenIntent()
}

/* STATE ----------------------------------------------------------------- */
@Immutable
sealed class VerifyScreenState : UiState {
    data object Init : VerifyScreenState()
    data object Loading : VerifyScreenState()
    data object Success : VerifyScreenState()  // UI ловит и открывает StartScreen
    data class Error(val message: String) : VerifyScreenState()
}