package dev.angryl1on.vetclinic.domain.usecase.appointmentsservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.appointment.AppointmentRequest

interface CreateAppointmentUseCase : UseCase {
    suspend operator fun invoke(model: AppointmentRequest): Result<Boolean>
}
