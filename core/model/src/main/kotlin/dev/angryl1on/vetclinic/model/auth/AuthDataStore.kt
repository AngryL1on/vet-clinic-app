package dev.angryl1on.vetclinic.model.auth

data class AuthDataStore(
    val id: Long?,
    val accessToken: String?,
    val refreshToken: String?,
    val accessTokenExpiresIn: Int?,
    val refreshTokenExpiresIn: Int?
)
