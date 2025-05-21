package dev.angryl1on.vetclinic.model.appointment

import kotlinx.serialization.Serializable

@Serializable
data class AppointmentResponse(
    val id: Long,
    val doctorId: Long,
    val doctorPhoto: String?,
    val doctorName: String,
    val petId: Long,
    val petName: String,
    val branchName: String,
    val appointmentDate: String,
    val appointmentStartTime: String,
    val appointmentType: String,
    val status: String,
    val comments: String?,
)
