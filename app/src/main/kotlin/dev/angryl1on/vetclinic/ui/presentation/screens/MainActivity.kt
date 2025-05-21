package dev.angryl1on.vetclinic.ui.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.angryl1on.appointment.presentation.screens.AppointmentScreen
import dev.angryl1on.history.presentation.screens.HistoryScreen
import dev.angryl1on.history.presentation.screens.VisitDetailsScreen
import dev.angryl1on.main.presentation.screens.MainScreen
import dev.angryl1on.profile.presentation.screens.AddPetScreen
import dev.angryl1on.profile.presentation.screens.EditPetScreen
import dev.angryl1on.profile.presentation.screens.PetisiansManagementScreen
import dev.angryl1on.profile.presentation.screens.ProfileManagmentScreen
import dev.angryl1on.profile.presentation.screens.ProfileScreen
import dev.angryl1on.vetclinic.auth.presentation.screens.LoginScreen
import dev.angryl1on.vetclinic.auth.presentation.screens.RegistrationScreen
import dev.angryl1on.vetclinic.auth.presentation.screens.SplashScreen
import dev.angryl1on.vetclinic.auth.presentation.screens.StartScreen
import dev.angryl1on.vetclinic.auth.presentation.screens.VerifyScreen
import dev.angryl1on.vetclinic.common.navigation.prettyName
import dev.angryl1on.vetclinic.common.navigation.routeName
import dev.angryl1on.vetclinic.domain.navigation.Route
import dev.angryl1on.vetclinic.model.medicalrecord.MedicalRecord
import dev.angryl1on.vetclinic.model.pet.PetResponse
import dev.angryl1on.vetclinic.ui.components.appbars.ToolBar
import dev.angryl1on.vetclinic.ui.components.appbars.TopBarError
import dev.angryl1on.vetclinic.ui.components.appbars.TopBarLoading
import dev.angryl1on.vetclinic.ui.components.navigation.BottomNavBar
import dev.angryl1on.vetclinic.ui.components.navigation.BottomNavItemData
import dev.angryl1on.vetclinic.ui.presentation.viewmodel.MainActivityViewModel
import dev.angryl1on.vetclinic.ui.presentation.viewmodel.UiScaffoldState
import dev.angryl1on.vetclinic.ui.provider.LocalSnackbarHostState
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme
import dev.angryl1on.vetclinic.ui.theme.White
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    private val bottomNavItems: List<BottomNavItemData> by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            VetClinicTheme {
                val coroutineScope = rememberCoroutineScope()
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                /**
                 * ViewModel
                 */
                val viewModel: MainActivityViewModel = koinViewModel()
                val scaffoldState by viewModel.scaffoldState.collectAsState()
                val userInfo by viewModel.userState.collectAsState()

                /**
                 * Нав. стэк
                 */
                val navBackStackEntry = navController.currentBackStackEntryAsState().value
                val currentRoute = navBackStackEntry?.destination?.route
                val screenTitle = currentRoute?.prettyName()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = White,
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        when (scaffoldState.appBarState) {
                            UiScaffoldState.AppBarState.None -> Unit
                            UiScaffoldState.AppBarState.Loading -> TopBarLoading()
                            is UiScaffoldState.AppBarState.Error -> TopBarError { viewModel.retry() }
                            is UiScaffoldState.AppBarState.Toolbar -> {
                                val toolbar =
                                    scaffoldState.appBarState as UiScaffoldState.AppBarState.Toolbar
                                ToolBar(
                                    modifier = Modifier.statusBarsPadding(),
                                    isMainScreen = toolbar.isMainScreen,
                                    iconLeft = toolbar.iconLeft,
                                    iconRight = toolbar.iconRight,
                                    imageAvatar = userInfo?.photoUrl,
                                    screenName = toolbar.screenName ?: screenTitle,
                                    firstName = userInfo?.firstName,
                                    lastName = userInfo?.lastName,
                                    onLeftIconClick = { if (toolbar.canNavigateBack) navController.popBackStack() },
                                    onRightIconClick = toolbar.onRightIconClick(navController)
                                )
                            }
                        }
                    },
                    bottomBar = {
                        if (scaffoldState.showBottomBar) {
                            BottomNavBar(
                                navController = navController,
                                scope = coroutineScope,
                                navItems = bottomNavItems,
                            )
                        }
                    }
                ) { innerPadding ->
                    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                        Box(Modifier.padding(innerPadding)) {
                            // ---------- Навигация ----------
                            NavHost(
                                navController = navController,
                                startDestination = Route.SplashScreen
                            ) {
                                composable<Route.SplashScreen> {
                                    SplashScreen(navController = navController)
                                }
                                composable<Route.StartScreen> {
                                    StartScreen(
                                        onLoginClick = { navController.navigate(Route.LoginScreen) },
                                        onRegisterClick = { navController.navigate(Route.RegistrationScreen) }
                                    )
                                }
                                composable<Route.RegistrationScreen> {
                                    RegistrationScreen(
                                        navController = navController
                                    )
                                }
                                composable(
                                    route = "${Route.VerifyScreen.routeName}?email={email}&masked={masked}",
                                    arguments = listOf(
                                        navArgument("email") { type = NavType.StringType },
                                        navArgument("masked") { type = NavType.StringType }
                                    )
                                ) {
                                    VerifyScreen(navController = navController)
                                }
                                composable<Route.LoginScreen> { LoginScreen(navController) }
                                composable<Route.MainScreen> {
                                    MainScreen(
                                        onAppointmentClick = {
                                            navController.navigate(Route.AppointmentScreen) {
                                                popUpTo(Route.MainScreen) { inclusive = true }
                                            }
                                        }
                                    )
                                }
                                composable<Route.AppointmentScreen> {
                                    AppointmentScreen(
                                        navController = navController
                                    )
                                }
                                composable<Route.HistoryScreen> { HistoryScreen(navController = navController) }
                                composable(Route.VisitDetailsScreen.routeName) { backStackEntry ->
                                    val record = backStackEntry
                                        .savedStateHandle
                                        .get<MedicalRecord>(Route.VisitDetailsScreen.ARG_RECORD)
                                        ?: error("MedicalRecord not found in SavedStateHandle")

                                    VisitDetailsScreen(
                                        record = record
                                    )
                                }

                                composable<Route.ProfileScreen> {
                                    ProfileScreen(
                                        onPersonalDataClick = {
                                            navController.navigate(Route.ProfileManagmentScreen)
                                        },
                                        onPetisiansManagementClick = {
                                            navController.navigate(Route.PetisiansManagementScreen)
                                        },
                                        onLogoutClick = {
                                            viewModel.logout(
                                                onComplete = {
                                                    navController.navigate(Route.StartScreen) {
                                                        popUpTo(Route.MainScreen) {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    )
                                }
                                composable<Route.ProfileManagmentScreen> { ProfileManagmentScreen() }
                                composable<Route.PetisiansManagementScreen> {
                                    PetisiansManagementScreen(
                                        onEditClick = { pet ->
                                            navController.navigate(Route.EditPetScreen.routeName)

                                            navController
                                                .getBackStackEntry(Route.EditPetScreen.routeName)
                                                .savedStateHandle["pet"] = pet
                                        }
                                    )
                                }
                                composable<Route.AddPetScreen> {
                                    AddPetScreen(
                                        onBack = {
                                            navController.popBackStack()
                                        }
                                    )
                                }
                                composable(Route.EditPetScreen.routeName) { backStackEntry ->
                                    val pet = backStackEntry
                                        .savedStateHandle
                                        .get<PetResponse>("pet")
                                        ?: error("Pet not found in SavedStateHandle")

                                    EditPetScreen(
                                        initialPet = pet,
                                        onBack = { navController.popBackStack() }
                                    )
                                }
                            }
                        }
                    }
                }

                /**
                 * Обновление состояния UI для текущего маршрута
                 * обновляем только при смене маршрута
                 */
                LaunchedEffect(
                    currentRoute,
                    navController.previousBackStackEntry,
                    navBackStackEntry
                ) {
                    viewModel.updateForRoute {
                        onRouteChanged(
                            currentRoute = currentRoute
                        )
                    }
                }
            }
        }
    }
}
