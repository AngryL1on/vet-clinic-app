package dev.angryl1on.vetclinic.domain.usecase.petservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.pet.PetRequest
import dev.angryl1on.vetclinic.model.pet.PetResponse

interface EditPetUseCase : UseCase {
    suspend operator fun invoke(id: Long, model: PetRequest): Result<PetResponse>
}
