package dev.angryl1on.profile.presentation.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dev.angryl1on.vetclinic.common.presentation.ModelIntent
import dev.angryl1on.vetclinic.common.presentation.Reducer
import dev.angryl1on.vetclinic.common.presentation.UiState
import dev.angryl1on.vetclinic.common.presentation.ViewModel
import dev.angryl1on.vetclinic.domain.usecase.authservice.GetUserInfoUseCase
import dev.angryl1on.vetclinic.model.ErrorResponse
import dev.angryl1on.vetclinic.model.auth.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel(
    private val getUserInfoUseCase: GetUserInfoUseCase

) : ViewModel<ProfileScreenState, ProfileScreenIntent>() {
    // Создайте копию Reducer для управления экраном
    private val reducer = ProfileScreenReducer(ProfileScreenState.Init)

    // Состояние, которое должно быть передано в UI
    override val state: Flow<ProfileScreenState>
        get() = reducer.state

    private val _userDataState: MutableStateFlow<UserInfo?> = MutableStateFlow(null)

    val userDataState: StateFlow<UserInfo?> = _userDataState.asStateFlow()

    init {
        sendIntent(ProfileScreenIntent.Load)
    }

    private fun sendIntent(profileScreenIntent: ProfileScreenIntent) {
        when(profileScreenIntent) {
            is ProfileScreenIntent.Load -> {
                viewModelScope.launch {
                    reducer.sendIntent(ProfileScreenIntent.Load)
                    val result = getUserInfoUseCase()
                    result.onSuccess { userInfo ->
                        _userDataState.value = userInfo
                        reducer.sendIntent(ProfileScreenIntent.LoadSuccess(userInfo))
                        Timber.d(userInfo.toString())
                    }.onFailure { error ->
                        reducer.setState(ProfileScreenState.Error(error.message))
                    }
                }
            }

            else -> {
                reducer.sendIntent(profileScreenIntent)
            }
        }
        reducer.sendIntent(profileScreenIntent)
    }

    private suspend fun getInfo(): Result<UserInfo> {
        Timber.d("getInfo called")
        return getUserInfoUseCase()
    }

    private class ProfileScreenReducer(initial: ProfileScreenState.Init) :
        Reducer<ProfileScreenState, ProfileScreenIntent>(
            initial
        ) {

        /**
         * Обрабатывает значение [intent] и соответственно обновляет состояние экрана.
         *
         * @param oldState Предыдущее состояние до обработки действия.
         * @param intent Действие, которое запускает изменение состояния.
         */
        override fun reduce(oldState: ProfileScreenState, intent: ProfileScreenIntent) {
            when (intent) {
                is ProfileScreenIntent.Load -> {
                    setState(ProfileScreenState.Loading)
                }

                is ProfileScreenIntent.LoadSuccess -> {
                    setState(ProfileScreenState.Success)
                }

                is ProfileScreenIntent.LoadFailure -> {
                    setState(ProfileScreenState.Error(message = null))
                }
            }
        }
    }
}

@Immutable
sealed class ProfileScreenIntent : ModelIntent {
    data object Load : ProfileScreenIntent()
    data class LoadSuccess(val model: UserInfo) : ProfileScreenIntent()
    data class LoadFailure(val error: ErrorResponse) : ProfileScreenIntent()
}

@Immutable
sealed class ProfileScreenState : UiState {
    data object Init : ProfileScreenState()
    data object Loading : ProfileScreenState()
    data object Success : ProfileScreenState()
    data class Error(val message: String?) : ProfileScreenState()
}
