package dev.angryl1on.vetclinic.network.petservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.petservice.UploadPhotoUseCase
import dev.angryl1on.vetclinic.model.pet.PetResponse
import dev.angryl1on.vetclinic.network.petservice.PetService
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport
import java.io.File

class UploadPhotoUseCaseImpl(
    private val petService: PetService,
    private val tokenSupport: TokenSupport
) : UploadPhotoUseCase {

    override suspend fun invoke(id: Long, file: File): Result<PetResponse> =
        tokenSupport.withTokenCheck { token ->
            petService.uploadPhoto(token = token, id = id, file = file)
        }
}
