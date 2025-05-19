package dev.angryl1on.vetclinic.model.medicalrecord

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class MedicalRecord(
    val id: Long,
    val doctorName: String,
    val photoDoctor: String? = null,
    val branchShortName: String,
    val date: String,
    val time: String,
    val type: String,
    val diagnosis: String,
    val treatment: String,
    val notes: String
) : Parcelable
