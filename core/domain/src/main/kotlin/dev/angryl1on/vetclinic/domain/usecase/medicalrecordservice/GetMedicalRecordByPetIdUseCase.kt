package dev.angryl1on.vetclinic.domain.usecase.medicalrecordservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.medicalrecord.MedicalRecord

interface GetMedicalRecordByPetIdUseCase : UseCase {
    suspend operator fun invoke(id: Long): Result<List<MedicalRecord>>
}
