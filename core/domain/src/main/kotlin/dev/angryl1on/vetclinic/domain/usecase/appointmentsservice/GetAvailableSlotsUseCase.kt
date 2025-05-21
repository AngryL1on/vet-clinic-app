package dev.angryl1on.vetclinic.domain.usecase.appointmentsservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase

interface GetAvailableSlotsUseCase : UseCase {
    suspend operator fun invoke(
        doctorId: Long,
        date: String,
        type: String
    ): Result<List<String>>
}
