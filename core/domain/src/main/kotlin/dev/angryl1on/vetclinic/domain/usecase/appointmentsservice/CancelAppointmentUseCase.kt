package dev.angryl1on.vetclinic.domain.usecase.appointmentsservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase

interface CancelAppointmentUseCase : UseCase {
    suspend operator fun invoke(appointmentId: Long): Result<Unit>
}
