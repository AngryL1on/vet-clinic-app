package dev.angryl1on.vetclinic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.angryl1on.appointment.presentation.screens.AppointmentScreen
import dev.angryl1on.history.presentation.screens.HistoryScreen
import dev.angryl1on.main.presentation.screens.MainScreen
import dev.angryl1on.profile.presentation.screens.ProfileScreen
import dev.angryl1on.vetclinic.auth.presentation.screens.LoginScreen
import dev.angryl1on.vetclinic.auth.presentation.screens.StartScreen
import dev.angryl1on.vetclinic.domain.navigation.Route
import dev.angryl1on.vetclinic.ui.components.navigation.BottomNavBar
import dev.angryl1on.vetclinic.ui.components.navigation.BottomNavItemData
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val bottomNavItems: List<BottomNavItemData> by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VetClinicTheme {
                val coroutineScope = rememberCoroutineScope()
                val navController = rememberNavController()
                val isLoggedIn = false

                val startDestination = if (isLoggedIn) {
                    Route.MainScreen
                } else {
                    Route.StartScreen
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (isLoggedIn) {
                            BottomNavBar(
                                navController = navController,
                                scope = coroutineScope,
                                navItems = bottomNavItems,
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            composable<Route.StartScreen> {
                                StartScreen(
                                    onLoginClick = {
                                        navController.navigate(Route.LoginScreen)
                                    },
                                    onRegisterClick = {}
                                )
                            }
                            composable<Route.LoginScreen> {
                                LoginScreen(
                                    onLoginClick = { _, _ ->

                                    }
                                )
                            }
                            composable<Route.MainScreen> {
                                MainScreen()
                            }
                            composable<Route.AppointmentScreen> {
                                AppointmentScreen()
                            }
                            composable<Route.HistoryScreen> {
                                HistoryScreen()
                            }
                            composable<Route.ProfileScreen> {
                                ProfileScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
