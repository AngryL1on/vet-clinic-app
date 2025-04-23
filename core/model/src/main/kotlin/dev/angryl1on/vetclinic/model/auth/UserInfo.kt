package dev.angryl1on.vetclinic.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: Long,
    val firstName: String?,
    val lastName: String?,
    val photoUrl: String?,
    val email: String,
    val enabled: Boolean,
)
