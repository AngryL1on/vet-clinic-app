package dev.angryl1on.vetclinic.model.pet

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PetResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("birthDate")
    val birthDate: String,
    @SerialName("animalType")
    val animalType: String,
    @SerialName("breed")
    val breed: String,
    @SerialName("photoUrl")
    val photoUrl: String?
) : Parcelable
