package dev.angryl1on.vetclinic.network.petservice.usecase

import android.os.Build
import androidx.annotation.RequiresExtension
import dev.angryl1on.vetclinic.database.dao.PetDao
import dev.angryl1on.vetclinic.domain.usecase.petservice.DeletePetUseCase
import dev.angryl1on.vetclinic.network.petservice.PetService
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeletePetUseCaseImpl(
    private val petService: PetService,
    private val petDao: PetDao,
    private val tokenSupport: TokenSupport
) : DeletePetUseCase {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun invoke(id: Long): Result<Boolean> =
        withContext(Dispatchers.IO) {
            tokenSupport.withTokenCheck { token ->
                runCatching { petService.delete(id = id, token = token) }
            }.onSuccess { deletedOnServer ->
                if (deletedOnServer) {
                    petDao.delete(id)
                }
            }
        }
}
