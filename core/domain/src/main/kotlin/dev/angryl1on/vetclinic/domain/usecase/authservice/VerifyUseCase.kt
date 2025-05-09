package dev.angryl1on.vetclinic.domain.usecase.authservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.auth.VerifyRegisterData

interface VerifyUseCase : UseCase {
    suspend operator fun invoke(model: VerifyRegisterData): Result<String>
}
