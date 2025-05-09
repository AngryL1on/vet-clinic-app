package dev.angryl1on.vetclinic.ui.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dev.angryl1on.vetclinic.common.presentation.ModelIntent
import dev.angryl1on.vetclinic.common.presentation.Reducer
import dev.angryl1on.vetclinic.common.presentation.UiState
import dev.angryl1on.vetclinic.common.presentation.ViewModel
import dev.angryl1on.vetclinic.data.auth.AuthenticationDataStore
import dev.angryl1on.vetclinic.database.VetClinicDatabase
import dev.angryl1on.vetclinic.domain.navigation.Route
import dev.angryl1on.vetclinic.domain.usecase.authservice.GetUserInfoUseCase
import dev.angryl1on.vetclinic.model.auth.UserInfo
import dev.angryl1on.vetclinic.ui.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val getCurrentUserUseCase: GetUserInfoUseCase,
    private val authDataStore: AuthenticationDataStore,
    private val database: VetClinicDatabase
) : ViewModel<MainActivityState, MainActivityIntent>() {

    /**
     * MVI-infrastructure
     */
    private val reducer = MainActivityReducer(MainActivityState.Init)
    override val state: Flow<MainActivityState>
        get() = reducer.state

    /**
     *  Отдельный поток для UserInfo (успешная загрузка)
     */
    private val _userState = MutableStateFlow<UserInfo?>(null)
    val userState: StateFlow<UserInfo?> = _userState.asStateFlow()

    /**
     *  Состояние скэффолда (top/bottom bars)
     */
    private val _scaffoldState = MutableStateFlow(UiScaffoldState())
    val scaffoldState: StateFlow<UiScaffoldState> = _scaffoldState.asStateFlow()

    init {
        loadUser()
    }

    fun retry() = loadUser()

    /**
     * Вызываем из Activity, когда меняется visible‑route
     */
    fun updateForRoute(update: UiScaffoldState.() -> UiScaffoldState) {
        _scaffoldState.value = _scaffoldState.value.update()
    }

    private fun loadUser() {
        viewModelScope.launch {
            reducer.sendIntent(MainActivityIntent.Load)
            getCurrentUserUseCase()
                .onSuccess { user ->
                    _userState.value = user
                    reducer.sendIntent(MainActivityIntent.LoadSuccess(user))
                }
                .onFailure { reducer.sendIntent(MainActivityIntent.LoadFailure(it.message)) }
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            authDataStore.clear()
            onComplete()
        }
    }

    private class MainActivityReducer(initial: MainActivityState.Init) :
        Reducer<MainActivityState, MainActivityIntent>(initial) {

        override fun reduce(oldState: MainActivityState, intent: MainActivityIntent) {
            when (intent) {
                is MainActivityIntent.Load -> setState(MainActivityState.Loading)
                is MainActivityIntent.LoadSuccess -> setState(MainActivityState.Success)
                is MainActivityIntent.LoadFailure -> setState(MainActivityState.Error(intent.error))
            }
        }
    }
}

/**
 *  UI‑State вокруг Scaffold
 */
@Immutable
data class UiScaffoldState(
    val appBarState: AppBarState = AppBarState.None,
    val showBottomBar: Boolean = false
) {

    /** Обновляем, когда активный маршрут изменился. */
    fun onRouteChanged(
        currentRoute: String?
    ): UiScaffoldState {
        // если route не известен — ничего не меняем
        if (currentRoute == null) return this

        val newState = copy(
            appBarState = calculateAppBar(currentRoute),
            showBottomBar = !currentRoute.isAuthRoute()
        )

        // возвращаем либо новый, либо старый state
        return if (newState != this) newState else this
    }

    /* ---------------- Вложенные типы ---------------- */

    sealed interface AppBarState {
        /** Экранов без верхней панели. */
        data object None : AppBarState

        /** Идёт загрузка профиля. */
        data object Loading : AppBarState

        /** Произошла ошибка профиля. */
        data class Error(val message: String? = null) : AppBarState

        /** Стандартный toolbar */
        data class Toolbar(
            val screenName: String?,
            val isMainScreen: Boolean,
            val canNavigateBack: Boolean,
            val iconLeft: Int?,
            val iconRight: Int?,
            val onRightIconClick: (navController: androidx.navigation.NavHostController) -> () -> Unit = { { } }
        ) : AppBarState
    }

    /* ---------------- Приватные утилиты ---------------- */

    private fun calculateAppBar(
        route: String
    ): AppBarState {
        if (route.isAuthRoute()) return AppBarState.None

        val toolBar = when (route.routeSuffix()) {
            "PetisiansManagementScreen" -> AppBarState.Toolbar(
                screenName = "Petisians Management",
                isMainScreen = false,
                canNavigateBack = true,
                iconLeft = R.drawable.ic_arrow_back,
                iconRight = R.drawable.ic_add,
                onRightIconClick = { navController ->
                    { navController.navigate(Route.AddPetScreen) }
                }
            )

            "EditPetScreen" -> AppBarState.Toolbar(
                screenName = "Edit pet",
                isMainScreen = false,
                canNavigateBack = true,
                iconLeft = R.drawable.ic_arrow_back,
                iconRight = null,
            )

            "AddPetScreen" -> AppBarState.Toolbar(
                screenName = "Add pet",
                isMainScreen = false,
                canNavigateBack = true,
                iconLeft = R.drawable.ic_arrow_back,
                iconRight = null,
            )

            "ProfileManagmentScreen" -> AppBarState.Toolbar(
                screenName = "Profile Management",
                isMainScreen = false,
                canNavigateBack = true,
                iconLeft = R.drawable.ic_arrow_back,
                iconRight = null,
            )

            "MainScreen" -> AppBarState.Toolbar(
                screenName = "Main Screen",
                isMainScreen = true,
                canNavigateBack = false,
                iconLeft = null,
                iconRight = R.drawable.ic_notifications
            )

            else -> AppBarState.Toolbar(
                screenName = null,
                isMainScreen = false,
                canNavigateBack = false,
                iconLeft = null,
                iconRight = null
            )
        }
        return toolBar
    }
}

/**
 * INTENTS
 */
@Immutable
sealed class MainActivityIntent : ModelIntent {
    data object Load : MainActivityIntent()
    data class LoadSuccess(val model: UserInfo) : MainActivityIntent()
    data class LoadFailure(val error: String?) : MainActivityIntent()
}

/**
 * STATE
 */
@Immutable
sealed class MainActivityState : UiState {
    data object Init : MainActivityState()
    data object Loading : MainActivityState()
    data object Success : MainActivityState()
    data class Error(val message: String?) : MainActivityState()
}

/**
 * Extension‑helpers
 */
private fun String.routeSuffix(): String = substringAfterLast('.')

private fun String.isAuthRoute(): Boolean = routeSuffix() in AUTH_ROUTE_SUFFIXES

private val AUTH_ROUTE_SUFFIXES = setOf(
    "SplashScreen",
    "LoginScreen",
    "RegistrationScreen",
    "${Route.VerifyScreen}?email={email}&masked={masked}",
    "StartScreen"
)
