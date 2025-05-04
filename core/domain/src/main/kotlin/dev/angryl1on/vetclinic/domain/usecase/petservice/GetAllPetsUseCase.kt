package dev.angryl1on.vetclinic.domain.usecase.petservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.pet.PetResponse

interface GetAllPetsUseCase : UseCase {
    suspend operator fun invoke() : Result<List<PetResponse>>
}
