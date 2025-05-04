package dev.angryl1on.vetclinic.network.petservice.usecase

import android.os.Build
import androidx.annotation.RequiresExtension
import dev.angryl1on.vetclinic.database.dao.PetDao
import dev.angryl1on.vetclinic.database.table.mapToEntity
import dev.angryl1on.vetclinic.database.table.mapToModel
import dev.angryl1on.vetclinic.domain.usecase.petservice.GetAllPetsUseCase
import dev.angryl1on.vetclinic.model.pet.PetResponse
import dev.angryl1on.vetclinic.network.petservice.PetService
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllPetsUseCaseImpl(
    private val petService: PetService,
    private val petDao: PetDao,
    private val tokenSupport: TokenSupport
) : GetAllPetsUseCase {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun invoke(): Result<List<PetResponse>> =
        // Переключаем весь блок на IO
        withContext(Dispatchers.IO) {
            runCatching {
                // 1) Сеть
                val petsFromServer = tokenSupport
                    .withTokenCheck { token -> petService.getAll(token) }
                    .getOrThrow()

                // 2) Синхронизация кэша
                petDao.deleteAll()
                petDao.insertAll(petsFromServer.map { it.mapToEntity() })

                petsFromServer
            }.recoverCatching { e ->
                // 3) Фолбэк на Room
                val cached = petDao.getPets().map { it.mapToModel() }
                cached.ifEmpty { throw e }
            }
        }
}
