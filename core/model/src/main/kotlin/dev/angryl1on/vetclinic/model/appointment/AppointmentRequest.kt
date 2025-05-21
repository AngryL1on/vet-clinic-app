package dev.angryl1on.vetclinic.model.appointment

import kotlinx.serialization.Serializable

@Serializable
data class AppointmentRequest(
    val doctorId: Long,
    val petId: Long,
    val appointmentDate: String,
    val appointmentStartTime: String,
    val appointmentType: String
)
