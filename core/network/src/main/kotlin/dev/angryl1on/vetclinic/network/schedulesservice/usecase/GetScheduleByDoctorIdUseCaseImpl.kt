package dev.angryl1on.vetclinic.network.schedulesservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.schedulesservice.GetScheduleByDoctorIdUseCase
import dev.angryl1on.vetclinic.model.schedule.ScheduleResponse
import dev.angryl1on.vetclinic.network.schedulesservice.SchedulesService
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport

class GetScheduleByDoctorIdUseCaseImpl(
    private val schedulesService: SchedulesService,
    private val tokenSupport: TokenSupport
) : GetScheduleByDoctorIdUseCase {
    override suspend fun invoke(doctorId: Long): Result<List<ScheduleResponse>> =
        tokenSupport.withTokenCheck { token ->
            schedulesService.getScheduleByDoctorId(token = token, doctorId = doctorId)
        }
}
