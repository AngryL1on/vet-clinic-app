package dev.angryl1on.vetclinic.data.auth

import dev.angryl1on.vetclinic.model.auth.AuthDataStore
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс для авторизации пользователя, сохранение информации.
 */
interface AuthenticationDataStore {

    /**
     * Метод, который возвращает поток данных о пользователе.
     */
    suspend fun fetchUsers(): Flow<AuthDataStore>

    /**
     * Метод, который устанавливает пользователь.
     */
    suspend fun setUserId(userId: Long)

    /**
     * Метод, который устанавливает токен доступа.
     */
    suspend fun setAccessToken(accessToken: String)

    /**
     * Метод, который устанавливает токен обновления.
     */
    suspend fun setRefreshToken(refreshToken: String)

    /**
     * Метод, который устанавливает время жизни токена доступа.
     */
    suspend fun setAccessTokenExpiresIn(accessTokenExpiresIn: Int)

    /**
     * Метод, который устанавливает время жизни токен обновления.
     */
    suspend fun setRefreshTokenExpiresIn(refreshTokenExpiresIn: Int)

    /**
     * Метод, который сохраняет пользователя
     */
    suspend fun updateUser(user: AuthDataStore)

    suspend fun clear()
}
