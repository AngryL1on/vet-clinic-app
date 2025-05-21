package dev.angryl1on.vetclinic.domain.usecase.userservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.user.DoctorResponseForSelectInAppointment

interface GetDoctorsByBranchIdUseCase : UseCase {
    suspend operator fun invoke(idBranch: Long): Result<List<DoctorResponseForSelectInAppointment>>
}
