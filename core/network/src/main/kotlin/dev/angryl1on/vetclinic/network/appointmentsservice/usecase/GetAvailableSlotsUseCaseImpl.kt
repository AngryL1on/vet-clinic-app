package dev.angryl1on.vetclinic.network.appointmentsservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.appointmentsservice.GetAvailableSlotsUseCase
import dev.angryl1on.vetclinic.network.appointmentsservice.AppointmentsService
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport

class GetAvailableSlotsUseCaseImpl(
    private val appointmentsService: AppointmentsService,
    private val tokenSupport: TokenSupport
) : GetAvailableSlotsUseCase {
    override suspend fun invoke(doctorId: Long, date: String, type: String): Result<List<String>> =
        tokenSupport.withTokenCheck { token ->
            appointmentsService.getAvailableSlots(
                token = token,
                doctorId = doctorId,
                date = date,
                type = type
            )
        }
}
