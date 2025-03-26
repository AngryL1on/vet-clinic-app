package dev.angryl1on.vetclinic.model.auth

import kotlinx.serialization.Serializable

/**
 * Класс данных, который содержит токен обновления.
 *
 * @property [refreshToken] Токен обновления.
 */
@Serializable
data class RefreshToken(val refreshToken: String)
