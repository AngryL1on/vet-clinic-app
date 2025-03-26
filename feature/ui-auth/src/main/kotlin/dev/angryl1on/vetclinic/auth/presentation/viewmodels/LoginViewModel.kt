package dev.angryl1on.vetclinic.auth.presentation.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dev.angryl1on.vetclinic.common.presentation.ModelIntent
import dev.angryl1on.vetclinic.common.presentation.Reducer
import dev.angryl1on.vetclinic.common.presentation.UiState
import dev.angryl1on.vetclinic.common.presentation.ViewModel
import dev.angryl1on.vetclinic.common.presentation.validation.EmailValidator
import dev.angryl1on.vetclinic.domain.usecase.authservice.SignInUseCase
import dev.angryl1on.vetclinic.model.ErrorResponse
import dev.angryl1on.vetclinic.model.auth.AuthData
import dev.angryl1on.vetclinic.model.auth.AuthNetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel(
    private val signIn: SignInUseCase,
    private val emailValidator: EmailValidator,
) : ViewModel<LoginScreenState, LoginScreenIntent>() {

    // Создайте копию Reducer для управления экраном
    private val reducer = LoginScreenReducer(LoginScreenState.Init)

    // Состояние, которое должно быть передано в UI
    override val state: Flow<LoginScreenState>
        get() = reducer.state

    private val _authDataState: MutableStateFlow<AuthData> = MutableStateFlow(
        AuthData(
            email = String(),
            password = String()
        )
    )

    /**
     * Поток состояния используется для наблюдения за изменениями в почте и пароле от пользовательского интерфейса
     */
    val authDataState: StateFlow<AuthData> = _authDataState.asStateFlow()

    /**
     * Обновляет значение поля ввода электронной почты
     */
    fun changeEmail(email: String) {
        Timber.d("changeLogin: $email")
        _authDataState.value = authDataState.value.copy(email = email)
    }

    /**
     * Обновляет значение поля ввода пароля
     */
    fun changePassword(password: String) {
        Timber.d("changePassword: $password")
        _authDataState.value = authDataState.value.copy(password = password)
        clearErrors()
    }

    /**
     * Ошибки очистки на экране
     */
    private fun clearErrors() {
        Timber.d("clearErrors called")
        reducer.setState(LoginScreenState.Init)
    }

    init {
        sendIntent(LoginScreenIntent.Load)
    }

    /**
     * Метод отправки Intent в Reducer
     */
    private fun sendIntent(loginScreenIntent: LoginScreenIntent) {
        reducer.sendIntent(loginScreenIntent)
    }

    fun signIn() {
        Timber.d("signIn called")

        val emailError = validateEmail(authDataState.value.email)
        val passwordError = validatePassword(authDataState.value.password)

        if (emailError != null || passwordError != null) {
            reducer.setState(LoginScreenState.ValidationError(emailError, passwordError))
            return
        }

        viewModelScope.launch {
            reducer.setState(LoginScreenState.Loading)

            Timber.d("signInUseCase called. Login: ${authDataState.value.email}; Password: ${authDataState.value.password}")

            val signInResult = signIn.invoke(model = authDataState.value)

            if (signInResult.isFailure) {
                signInResult.exceptionOrNull()?.message.let { resultMessage ->
                    Timber.e("signIn result is failure. $resultMessage")
                    reducer.setState(LoginScreenState.Error(message = resultMessage))
                }
            } else {
                Timber.d("signIn result is success")
                reducer.setState(LoginScreenState.Success)
            }
        }
    }

    /**
     * Проверяет поле email
     */
    private fun validateEmail(email: String): String? {
        Timber.d("validateEmail: $email")
        return if (email.isEmpty() || email.isBlank()) "Поле электронной почты не может быть пустым" else null
    }

    /**
     * Проверяет поле ввода пароля
     */
    private fun validatePassword(password: String): String? {
        Timber.d("validatePassword: $password")
        return if (password.isEmpty() || password.isBlank()) "Поле пароля не может быть пустым" else null
    }

    private class LoginScreenReducer(initial: LoginScreenState.Init) :
        Reducer<LoginScreenState, LoginScreenIntent>(
            initial
        ) {

        /**
         * Обрабатывает значение [intent] и соответственно обновляет состояние экрана.
         *
         * @param oldState Предыдущее состояние до обработки действия.
         * @param intent Действие, которое запускает изменение состояния.
         */
        override fun reduce(oldState: LoginScreenState, intent: LoginScreenIntent) {
            when (intent) {
                is LoginScreenIntent.Load -> {
                    setState(LoginScreenState.Loading)
                }

                is LoginScreenIntent.LoadSuccess -> {
                    setState(LoginScreenState.Success)
                }

                is LoginScreenIntent.LoadFailure -> {
                    setState(LoginScreenState.Error(message = null))
                }
            }
        }
    }

    companion object {
        const val INCORRECT_LOGIN_OR_PASSWORD = "Неправильный логин или пароль"
        const val INVALID_FIELDS = "Не все поля заполнены корректно"
    }
}

@Immutable
sealed class LoginScreenIntent : ModelIntent {
    data object Load : LoginScreenIntent()
    data class LoadSuccess(val model: AuthNetworkResponse) : LoginScreenIntent()
    data class LoadFailure(val error: ErrorResponse) : LoginScreenIntent()
}

@Immutable
sealed class LoginScreenState : UiState {
    data object Init : LoginScreenState()
    data object Loading : LoginScreenState()
    data object Success : LoginScreenState()
    data class Error(val message: String?) : LoginScreenState()
    data class ValidationError(val loginError: String?, val passwordError: String?) :
        LoginScreenState()
}
