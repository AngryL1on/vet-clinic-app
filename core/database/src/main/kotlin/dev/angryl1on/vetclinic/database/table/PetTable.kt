package dev.angryl1on.vetclinic.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.angryl1on.vetclinic.model.pet.PetResponse

@Entity(tableName = "pets")
data class PetTable(
    @PrimaryKey
    val id: Long,
    val name: String,
    val birthDate: String,
    val animalType: String,
    val breed: String,
    val photoUrl: String? = null
)

fun PetTable.mapToModel(): PetResponse =
    PetResponse(
        id = id,
        name = name,
        birthDate = birthDate,
        animalType = animalType,
        breed = breed,
        photoUrl = photoUrl
    )

fun PetResponse.mapToEntity(): PetTable =
    PetTable(
        id = id,
        name = name,
        birthDate = birthDate,
        animalType = animalType,
        breed = breed,
        photoUrl = photoUrl
    )
