package dev.angryl1on.vetclinic.model.schedule

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    val id: Long,
    val doctorId: Long,
    val date: String,
    val startTime: String,
    val endTime: String
)
