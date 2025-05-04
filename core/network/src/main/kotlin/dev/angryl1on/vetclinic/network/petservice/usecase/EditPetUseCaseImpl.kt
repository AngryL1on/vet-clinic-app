package dev.angryl1on.vetclinic.network.petservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.petservice.EditPetUseCase
import dev.angryl1on.vetclinic.model.pet.PetRequest
import dev.angryl1on.vetclinic.model.pet.PetResponse
import dev.angryl1on.vetclinic.network.petservice.PetService
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport

class EditPetUseCaseImpl(
    private val petService: PetService,
    private val tokenSupport: TokenSupport
) : EditPetUseCase {
    override suspend fun invoke(id: Long, model: PetRequest): Result<PetResponse> =
        tokenSupport.withTokenCheck { token ->
            petService.edit(token = token, id = id, model = model)
        }
}
