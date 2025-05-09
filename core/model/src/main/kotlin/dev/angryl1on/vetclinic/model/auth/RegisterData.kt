package dev.angryl1on.vetclinic.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterData(
    val firstName: String,
    val lastName: String,
    val number: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
