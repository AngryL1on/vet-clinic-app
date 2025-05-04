package dev.angryl1on.vetclinic.domain.usecase.petservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.pet.PetResponse
import java.io.File

interface UploadPhotoUseCase : UseCase {
    suspend operator fun invoke(id: Long, file: File): Result<PetResponse>
}
