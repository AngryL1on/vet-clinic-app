package dev.angryl1on.vetclinic.domain.usecase.petservice

import dev.angryl1on.vetclinic.model.pet.PetResponse
import kotlinx.coroutines.flow.Flow

interface ObservePetsUseCase {
    operator fun invoke(): Flow<List<PetResponse>>
}
