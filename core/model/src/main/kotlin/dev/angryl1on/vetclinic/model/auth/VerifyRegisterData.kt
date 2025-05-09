package dev.angryl1on.vetclinic.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class VerifyRegisterData(
    val email: String,
    val verificationCode: String
)
