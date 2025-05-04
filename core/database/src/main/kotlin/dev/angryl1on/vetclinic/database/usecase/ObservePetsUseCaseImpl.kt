package dev.angryl1on.vetclinic.database.usecase

import dev.angryl1on.vetclinic.database.dao.PetDao
import dev.angryl1on.vetclinic.database.table.mapToModel
import dev.angryl1on.vetclinic.domain.usecase.petservice.ObservePetsUseCase
import dev.angryl1on.vetclinic.model.pet.PetResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObservePetsUseCaseImpl(
    private val petDao: PetDao
) : ObservePetsUseCase {

    override fun invoke(): Flow<List<PetResponse>> =
        petDao.observeAll()
            .map { list -> list.map { it.mapToModel() } }
}
