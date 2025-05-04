package dev.angryl1on.vetclinic.network.petservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.petservice.CreatePetUseCase
import dev.angryl1on.vetclinic.model.pet.PetRequest
import dev.angryl1on.vetclinic.model.pet.PetResponse
import dev.angryl1on.vetclinic.network.petservice.PetService
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport

class CreatePetUseCaseImpl(
    private val petService: PetService,
    private val tokenSupport: TokenSupport
) : CreatePetUseCase {
    override suspend fun invoke(model: PetRequest): Result<PetResponse> =
        tokenSupport.withTokenCheck { token ->
            petService.create(token = token, model = model)
        }
}
