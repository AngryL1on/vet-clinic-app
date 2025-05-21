package dev.angryl1on.vetclinic.network.branchesservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.branchesservice.GetBranchByServiceNameUseCase
import dev.angryl1on.vetclinic.model.branches.BranchResponse
import dev.angryl1on.vetclinic.network.branchesservice.BranchesService
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport

class GetBranchByServiceNameUseCaseImpl(
    private val branchesService: BranchesService,
    private val tokenSupport: TokenSupport
) : GetBranchByServiceNameUseCase {
    override suspend fun invoke(service: String): Result<List<BranchResponse>> =
        tokenSupport.withTokenCheck { token ->
            branchesService.getByServiceName(token = token, serviceName = service)
        }
}