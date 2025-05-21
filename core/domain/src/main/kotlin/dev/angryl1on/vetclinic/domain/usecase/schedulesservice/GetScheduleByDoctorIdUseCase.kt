package dev.angryl1on.vetclinic.domain.usecase.schedulesservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.schedule.ScheduleResponse

interface GetScheduleByDoctorIdUseCase : UseCase {
    suspend operator fun invoke(doctorId: Long): Result<List<ScheduleResponse>>
}
