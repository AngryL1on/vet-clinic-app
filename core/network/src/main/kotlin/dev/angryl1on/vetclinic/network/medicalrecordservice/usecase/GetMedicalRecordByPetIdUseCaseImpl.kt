package dev.angryl1on.vetclinic.network.medicalrecordservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.medicalrecordservice.GetMedicalRecordByPetIdUseCase
import dev.angryl1on.vetclinic.model.medicalrecord.MedicalRecord
import dev.angryl1on.vetclinic.network.medicalrecordservice.MedicalRecordService
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport

class GetMedicalRecordByPetIdUseCaseImpl(
    private val medicalRecordService: MedicalRecordService,
    private val tokenSupport: TokenSupport
) : GetMedicalRecordByPetIdUseCase {
    override suspend fun invoke(id: Long): Result<List<MedicalRecord>> =
        tokenSupport.withTokenCheck { token ->
            medicalRecordService.getById(token = token, id = id)
        }
}
