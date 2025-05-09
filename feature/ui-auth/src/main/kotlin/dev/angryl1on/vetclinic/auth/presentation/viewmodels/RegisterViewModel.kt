package dev.angryl1on.vetclinic.auth.presentation.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dev.angryl1on.vetclinic.common.presentation.ModelIntent
import dev.angryl1on.vetclinic.common.presentation.Reducer
import dev.angryl1on.vetclinic.common.presentation.UiState
import dev.angryl1on.vetclinic.common.presentation.ViewModel
import dev.angryl1on.vetclinic.domain.usecase.authservice.RegisterUseCase
import dev.angryl1on.vetclinic.model.auth.RegisterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel<RegisterScreenState, RegisterScreenIntent>() {

    /* MVI‑infrastructure */
    private val reducer = RegisterScreenReducer(RegisterScreenState.Init)
    override val state: Flow<RegisterScreenState>
        get() = reducer.state

    /* form fields */
    private val _firstName = MutableStateFlow("")
    private val _lastName = MutableStateFlow("")
    private val _number = MutableStateFlow("")
    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _confirm = MutableStateFlow("")

    /* errors */
    private val _firstNameErr = MutableStateFlow<String?>(null)
    private val _lastNameErr = MutableStateFlow<String?>(null)
    private val _numberErr = MutableStateFlow<String?>(null)
    private val _emailErr = MutableStateFlow<String?>(null)
    private val _passwordErr = MutableStateFlow<String?>(null)
    private val _confirmErr = MutableStateFlow<String?>(null)

    /* public flows */
    val firstName: StateFlow<String> get() = _firstName
    val lastName: StateFlow<String> get() = _lastName
    val number: StateFlow<String> get() = _number
    val email: StateFlow<String> get() = _email
    val password: StateFlow<String> get() = _password
    val confirm: StateFlow<String> get() = _confirm

    val firstNameErr: StateFlow<String?> get() = _firstNameErr
    val lastNameErr: StateFlow<String?> get() = _lastNameErr
    val numberErr: StateFlow<String?> get() = _numberErr
    val emailErr: StateFlow<String?> get() = _emailErr
    val passwordErr: StateFlow<String?> get() = _passwordErr
    val confirmErr: StateFlow<String?> get() = _confirmErr

    /* public callbacks */
    fun onFirstNameChanged(v: String) = dispatch(RegisterScreenIntent.FirstNameChanged(v))
    fun onLastNameChanged(v: String) = dispatch(RegisterScreenIntent.LastNameChanged(v))
    fun onNumberChanged(v: String) = dispatch(RegisterScreenIntent.NumberChanged(v))
    fun onEmailChanged(v: String) = dispatch(RegisterScreenIntent.EmailChanged(v))
    fun onPasswordChanged(v: String) = dispatch(RegisterScreenIntent.PasswordChanged(v))
    fun onConfirmChanged(v: String) = dispatch(RegisterScreenIntent.ConfirmChanged(v))

    fun submit() = dispatch(RegisterScreenIntent.Submit)

    /* dispatcher */
    private fun dispatch(intent: RegisterScreenIntent) {
        when (intent) {
            is RegisterScreenIntent.FirstNameChanged -> {
                _firstName.value = intent.value
                _firstNameErr.value = validateName(intent.value, "Имя")
            }

            is RegisterScreenIntent.LastNameChanged -> {
                _lastName.value = intent.value
                _lastNameErr.value = validateName(intent.value, "Фамилия")
            }

            is RegisterScreenIntent.NumberChanged -> {
                _number.value = intent.value
                _numberErr.value = validatePhone(intent.value)
            }

            is RegisterScreenIntent.EmailChanged -> {
                _email.value = intent.value
                _emailErr.value = validateEmail(intent.value)
            }

            is RegisterScreenIntent.PasswordChanged -> {
                _password.value = intent.value
                _passwordErr.value = validatePassword(intent.value)
                _confirmErr.value = validateConfirm(_confirm.value, intent.value)
            }

            is RegisterScreenIntent.ConfirmChanged -> {
                _confirm.value = intent.value
                _confirmErr.value = validateConfirm(intent.value, _password.value)
            }

            RegisterScreenIntent.Submit -> processSubmit()

            else -> {}
        }
        reducer.sendIntent(intent)
    }

    /* business logic */
    private fun processSubmit() {
        viewModelScope.launch {
            if (!isFormValid()) {
                reducer.sendIntent(RegisterScreenIntent.SubmitFailure("Заполните все обязательные поля"))
                // обновляем все ошибки сразу
                _firstNameErr.value = validateName(_firstName.value, "Имя")
                _lastNameErr.value = validateName(_lastName.value, "Фамилия")
                _numberErr.value = validatePhone(_number.value)
                _emailErr.value = validateEmail(_email.value)
                _passwordErr.value = validatePassword(_password.value)
                _confirmErr.value = validateConfirm(_confirm.value, _password.value)
                return@launch
            }

            reducer.sendIntent(RegisterScreenIntent.SubmitStart)

            val body = RegisterData(
                firstName = _firstName.value.trim(),
                lastName = _lastName.value.trim(),
                number = _number.value.trim(),
                email = _email.value.trim(),
                password = _password.value,
                confirmPassword = _confirm.value
            )

            registerUseCase(body)
                .onSuccess {
                    val original = _email.value.trim()
                    val masked = maskEmail(original)
                    reducer.sendIntent(RegisterScreenIntent.SubmitSuccess(original, masked))
                    clearForm()
                }
                .onFailure { t ->
                    reducer.sendIntent(RegisterScreenIntent.SubmitFailure(t.message.toString()))
                }

            Timber.tag("request Register").d(body.toString())
        }
    }

    private fun isFormValid(): Boolean =
        _firstName.value.isNotBlank() &&
                _lastName.value.isNotBlank() &&
                _number.value.isNotBlank() &&
                _email.value.isNotBlank() &&
                _password.value.isNotBlank() &&
                _confirm.value.isNotBlank() &&
                _password.value == _confirm.value

    private fun clearForm() {
        _firstName.value = ""
        _lastName.value = ""
        _number.value = ""
        _email.value = ""
        _password.value = ""
        _confirm.value = ""
    }

    /* validation helpers -------------------------------------------------- */
    private fun validateName(v: String, fieldName: String) = when {
        v.isBlank() -> "Введите $fieldName"
        v.any { it.isDigit() } -> "$fieldName не должно содержать цифр"
        else -> null
    }

    private fun validatePhone(v: String) = when {
        v.isBlank() -> "Введите номер телефона"
        !v.all { it.isDigit() } -> "Номер телефона должен содержать только цифры"
        v.length !in 10..15 -> "Длина номера должна быть 10–15 символов"
        else -> null
    }

    private fun validateEmail(v: String) = when {
        v.isBlank() -> "Введите e‑mail"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(v).matches() -> "Неверный формат e‑mail"
        else -> null
    }

    private fun validatePassword(v: String) = when {
        v.isBlank() -> "Введите пароль"
        v.length < 6 -> "Пароль должен быть не короче 6 символов"
        else -> null
    }

    private fun validateConfirm(confirm: String, password: String) = when {
        confirm.isBlank() -> "Подтвердите пароль"
        confirm != password -> "Пароли не совпадают"
        else -> null
    }

    private fun maskEmail(email: String): String {
        val parts = email.split("@")
        if (parts.size != 2) return email
        val name = parts[0]
        val maskedName = when {
            name.length <= 2 -> "*".repeat(name.length)
            else -> name.first() + "*".repeat(name.length - 2) + name.last()
        }
        return "$maskedName@${parts[1]}"
    }

    /* Reducer -------------------------------------------------------------- */
    private class RegisterScreenReducer(initial: RegisterScreenState) :
        Reducer<RegisterScreenState, RegisterScreenIntent>(initial) {

        override fun reduce(oldState: RegisterScreenState, intent: RegisterScreenIntent) {
            when (intent) {
                RegisterScreenIntent.SubmitStart ->
                    setState(RegisterScreenState.Loading)

                is RegisterScreenIntent.SubmitSuccess ->
                    setState(
                        RegisterScreenState.Success(
                            originalEmail = intent.originalEmail,
                            maskedEmail = intent.maskedEmail
                        )
                    )

                is RegisterScreenIntent.SubmitFailure ->
                    setState(RegisterScreenState.Error(intent.error))

                else -> Unit
            }
        }
    }
}

/* INTENTS ----------------------------------------------------------------- */
@Immutable
sealed class RegisterScreenIntent : ModelIntent {
    data class FirstNameChanged(val value: String) : RegisterScreenIntent()
    data class LastNameChanged(val value: String) : RegisterScreenIntent()
    data class NumberChanged(val value: String) : RegisterScreenIntent()
    data class EmailChanged(val value: String) : RegisterScreenIntent()
    data class PasswordChanged(val value: String) : RegisterScreenIntent()
    data class ConfirmChanged(val value: String) : RegisterScreenIntent()

    data object Submit : RegisterScreenIntent()
    data object SubmitStart : RegisterScreenIntent()
    data class SubmitSuccess(
        val originalEmail: String,
        val maskedEmail: String
    ) : RegisterScreenIntent()

    data class SubmitFailure(val error: String) : RegisterScreenIntent()
}

/* STATE ------------------------------------------------------------------- */
@Immutable
sealed class RegisterScreenState : UiState {
    data object Init : RegisterScreenState()
    data object Loading : RegisterScreenState()

    /** UI ловит это состояние и навигирует на VerifyScreen,
     *  передавая originalEmail (для логики) и maskedEmail (для отображения) */
    data class Success(
        val originalEmail: String,
        val maskedEmail: String
    ) : RegisterScreenState()

    data class Error(val message: String) : RegisterScreenState()
}