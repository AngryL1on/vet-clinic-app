package dev.angryl1on.vetclinic.network.appointmentsservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.appointmentsservice.GetAppointmentByScheduled
import dev.angryl1on.vetclinic.model.appointment.AppointmentResponse
import dev.angryl1on.vetclinic.network.appointmentsservice.AppointmentsService
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport

class GetAppointmentByScheduledImpl(
    private val appointmentsService: AppointmentsService,
    private val tokenSupport: TokenSupport
) : GetAppointmentByScheduled {
    override suspend fun invoke(): Result<List<AppointmentResponse>> =
        tokenSupport.withTokenCheck { token ->
            appointmentsService.appointmentByScheduled(token = token)
        }
}
