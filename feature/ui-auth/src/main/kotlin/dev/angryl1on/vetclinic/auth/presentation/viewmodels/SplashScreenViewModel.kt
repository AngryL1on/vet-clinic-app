package dev.angryl1on.vetclinic.auth.presentation.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dev.angryl1on.vetclinic.common.presentation.ModelIntent
import dev.angryl1on.vetclinic.common.presentation.Reducer
import dev.angryl1on.vetclinic.common.presentation.UiState
import dev.angryl1on.vetclinic.common.presentation.ViewModel
import dev.angryl1on.vetclinic.data.auth.AuthenticationDataStore
import dev.angryl1on.vetclinic.domain.usecase.authservice.RefreshTokenUseCase
import dev.angryl1on.vetclinic.model.ErrorResponse
import dev.angryl1on.vetclinic.model.auth.AuthNetworkResponse
import dev.angryl1on.vetclinic.model.auth.RefreshToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashScreenViewModel(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val authenticationDataStore: AuthenticationDataStore,
) : ViewModel<SplashScreenState, LoginScreenIntent>() {

    private val reducer = SplashScreenReducer(SplashScreenState.Init)

    override val state: Flow<SplashScreenState>
        get() = reducer.state

    /**
     * Проверяет токен доступа пользователя, и если токен находится в хранилище данных, то обновляет его и изменяет состояние экрана
     */
    fun verifyToken() {
        Timber.d("verifyToken called","Current state is $state")
        viewModelScope.launch {
            Timber.d("fetchUsers called")
            authenticationDataStore.fetchUsers().first().run {
                Timber.d("fetchUsers called","Current state is ${authenticationDataStore.fetchUsers().first()}")
                reducer.setState(refreshToken?.let { refreshToken ->
                    if (refreshTokenUseCase.invoke(RefreshToken(refreshToken)).isSuccess) {
                        SplashScreenState.Success
                    } else {
                        SplashScreenState.Failure
                    }
                } ?: SplashScreenState.Failure).also { state ->
                    Timber.d("Verification result is $state")
                }
            }
        }
    }

    private class SplashScreenReducer(initial: SplashScreenState) :
        Reducer<SplashScreenState, SplashScreenIntent>(initial) {
        override fun reduce(oldState: SplashScreenState, intent: SplashScreenIntent) {
            when (intent) {
                is SplashScreenIntent.Load -> {
                    setState(SplashScreenState.Success)
                }

                is SplashScreenIntent.LoadSuccess -> {
                    setState(SplashScreenState.Success)
                }

                is SplashScreenIntent.LoadFailure -> {
                    setState(SplashScreenState.Failure)
                }
            }
        }
    }
}

@Immutable
sealed class SplashScreenIntent : ModelIntent {
    data object Load : SplashScreenIntent()
    data class LoadSuccess(val model: AuthNetworkResponse) : SplashScreenIntent()
    data class LoadFailure(val error: ErrorResponse) : SplashScreenIntent()
}

@Immutable
sealed class SplashScreenState : UiState {
    data object Init : SplashScreenState()
    data object Success : SplashScreenState()
    data object Failure : SplashScreenState()
}
