package dev.angryl1on.vetclinic.network.authservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.authservice.VerifyUseCase
import dev.angryl1on.vetclinic.model.auth.VerifyRegisterData
import dev.angryl1on.vetclinic.network.authservice.AuthService

class VerifyUseCaseImpl(
    private val authService: AuthService
) : VerifyUseCase {

    override suspend fun invoke(model: VerifyRegisterData): Result<String> =
        authService.verify(model = model)
}
