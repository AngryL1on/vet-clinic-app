package dev.angryl1on.vetclinic.domain.usecase.branchesservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.branches.BranchResponse

interface GetBranchByServiceNameUseCase : UseCase {
    suspend operator fun invoke(service: String): Result<List<BranchResponse>>
}
