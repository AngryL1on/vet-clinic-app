package dev.angryl1on.vetclinic.common.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Reducer базовый класс
 * Ответственный за хранение и обработку состояния
 *
 * @param S - state
 * @param I - intent
 */
abstract class Reducer<S : UiState, I : ModelIntent>(initialVal: S) {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialVal)
    val state: StateFlow<S> get() = _state.asStateFlow()

    /**
     * Method for sending [intent]
     */
    fun sendIntent(intent: I) {
        reduce(_state.value, intent)
    }

    /**
     * Method for setting [newState]
     */
    fun setState(newState: S) {
        _state.tryEmit(newState)
    }

    /**
     * Take an [oldState] and [intent] and process it
     */
    abstract fun reduce(oldState: S, intent: I)
}
