package dev.angryl1on.vetclinic.domain.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object StartScreen: Route

    @Serializable
    data object LoginScreen: Route

    @Serializable
    data object RegistrationFlowScreen : Route

    @Serializable
    data object SplashScreen: Route

    @Serializable
    data object MainScreen: Route

    @Serializable
    data object AppointmentScreen: Route

    @Serializable
    data object HistoryScreen: Route

    @Serializable
    data object ProfileScreen: Route
}
