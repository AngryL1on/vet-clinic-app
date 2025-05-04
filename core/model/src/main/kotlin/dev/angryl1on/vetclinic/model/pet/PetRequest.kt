package dev.angryl1on.vetclinic.model.pet

import kotlinx.serialization.Serializable

@Serializable
data class PetRequest(
    val name: String,
    val animalType: String,
    val birthDate: String,
    val breed: String
)
