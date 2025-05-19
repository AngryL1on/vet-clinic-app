package dev.angryl1on.vetclinic.model.pet

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PetResponse(
    val id: Long,
    val name: String,
    val birthDate: String,
    val animalType: String,
    val breed: String,
    val photoUrl: String?
) : Parcelable
