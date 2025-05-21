package dev.angryl1on.vetclinic.model.user

import kotlinx.serialization.Serializable

@Serializable
data class DoctorResponseForSelectInAppointment(
    val id: Long,
    val fullName: String
)
