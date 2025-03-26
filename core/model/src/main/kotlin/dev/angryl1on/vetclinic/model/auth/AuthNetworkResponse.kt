package dev.angryl1on.vetclinic.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthNetworkResponse(
    val id: Long,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Int,
    val refreshTokenExpiresIn: Int
)

/**
 * Преобразует [AuthNetworkResponse] объект в [AuthDataStore] объект
 */
fun AuthNetworkResponse.asUserAuthDataStore(): AuthDataStore = with(this) {
    AuthDataStore(
        id = id,
        accessToken = accessToken,
        refreshToken = refreshToken,
        accessTokenExpiresIn = accessTokenExpiresIn,
        refreshTokenExpiresIn = refreshTokenExpiresIn
    )
}
