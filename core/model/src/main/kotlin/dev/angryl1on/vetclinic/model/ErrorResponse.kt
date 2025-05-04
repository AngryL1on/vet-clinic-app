package dev.angryl1on.vetclinic.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val statusCode: Int,
    val message: String?,
    val path: String? = null,
    val timestamp: String? = null,
)
