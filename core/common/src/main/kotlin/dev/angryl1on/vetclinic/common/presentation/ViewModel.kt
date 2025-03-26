package dev.angryl1on.vetclinic.common.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

/**
 * ViewModel Class для работы с MVI
 * через [UiState] и [ModelIntent]
 */
abstract class ViewModel<T : UiState, in I : ModelIntent> : ViewModel() {
    abstract val state: Flow<T>
}
