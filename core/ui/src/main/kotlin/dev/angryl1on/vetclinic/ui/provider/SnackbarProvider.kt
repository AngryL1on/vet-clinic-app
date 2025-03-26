package dev.angryl1on.vetclinic.ui.provider

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import timber.log.Timber

val LocalSnackbarHostState =
    compositionLocalOf<SnackbarHostState> { error("SnackbarHostState is not initialized") }

@Stable
suspend fun SnackbarHostState.showMessage(message: String) {
    Timber.d("SnackbarHost","showMessage: $message")
    showSnackbar(
        message = message,
        withDismissAction = true,
        duration = SnackbarDuration.Indefinite
    )
}