package dev.angryl1on.vetclinic.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthData(
    val email: String,
    val password: String,
)
