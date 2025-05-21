package dev.angryl1on.vetclinic.network.userservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.userservice.GetDoctorsByBranchIdUseCase
import dev.angryl1on.vetclinic.model.user.DoctorResponseForSelectInAppointment
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport
import dev.angryl1on.vetclinic.network.userservice.UserService

class GetDoctorsByBranchIdUseCaseImpl(
    private val userService: UserService,
    private val tokenSupport: TokenSupport
) : GetDoctorsByBranchIdUseCase {
    override suspend fun invoke(idBranch: Long): Result<List<DoctorResponseForSelectInAppointment>> =
        tokenSupport.withTokenCheck { token ->
            userService.getDoctorsByBranchId(token = token, branchId = idBranch)
        }
}
