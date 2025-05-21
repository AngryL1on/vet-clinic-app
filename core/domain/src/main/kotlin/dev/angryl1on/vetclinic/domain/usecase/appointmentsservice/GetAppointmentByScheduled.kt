package dev.angryl1on.vetclinic.domain.usecase.appointmentsservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.appointment.AppointmentResponse

interface GetAppointmentByScheduled : UseCase {
    suspend operator fun invoke(): Result<List<AppointmentResponse>>
}
