package dev.angryl1on.vetclinic.domain.usecase.petservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase

interface DeletePetUseCase : UseCase {
    suspend operator fun invoke(id: Long): Result<Boolean>
}
