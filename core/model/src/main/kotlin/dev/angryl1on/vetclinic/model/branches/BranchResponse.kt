package dev.angryl1on.vetclinic.model.branches

import kotlinx.serialization.Serializable

@Serializable
data class BranchResponse(
    val name: String,
    val shortName: String,
    val id: Long,
    val address: String,
    val phone: String,
    val email: String,
    val latitude: Float,
    val longitude: Float,
    val services: List<String>
)
