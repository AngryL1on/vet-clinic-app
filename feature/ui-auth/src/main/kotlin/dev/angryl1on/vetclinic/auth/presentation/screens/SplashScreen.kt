package dev.angryl1on.vetclinic.auth.presentation.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import coil.compose.AsyncImage
import dev.angryl1on.vetclinic.auth.presentation.viewmodels.SplashScreenState
import dev.angryl1on.vetclinic.auth.presentation.viewmodels.SplashScreenViewModel
import dev.angryl1on.vetclinic.domain.navigation.Route
import dev.angryl1on.vetclinic.ui.theme.Inputs
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel = koinViewModel(),
    navController: NavController,
) {
    val dimensions = LocalDimensions.current
    val state by viewModel.state.collectAsState(initial = SplashScreenState.Init)

    val scale = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(Unit) {
        viewModel.verifyToken()

        scale.animateTo(
            targetValue = 2.2f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = { curve ->
                    OvershootInterpolator(1f).getInterpolation(curve)
                }
            )
        )
    }

    LaunchedEffect(state) {
        when (state) {
            is SplashScreenState.Success -> {
                navController.navigate(Route.MainScreen) {
                    popUpTo(Route.LoginScreen) { inclusive = true }
                }
            }
            is SplashScreenState.Failure -> {
                navController.navigate(Route.StartScreen) {
                    popUpTo(Route.LoginScreen) { inclusive = true }
                }
            }
            else -> {
                /* Do nothing */
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Inputs),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = dev.angryl1on.vetclinic.ui.R.drawable.ic_logo,
            contentDescription = null,
            modifier = Modifier.size(dimensions.logoSize)
        )
    }
}
