package dev.angryl1on.vetclinic.domain.usecase.authservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.auth.RegisterData

interface RegisterUseCase : UseCase {
    suspend operator fun invoke(model: RegisterData): Result<String>
}
