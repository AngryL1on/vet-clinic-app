package dev.angryl1on.vetclinic.network.authservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.authservice.RegisterUseCase
import dev.angryl1on.vetclinic.model.auth.RegisterData
import dev.angryl1on.vetclinic.network.authservice.AuthService

class RegisterUseCaseImpl(
    private val authService: AuthService
) : RegisterUseCase {
    override suspend fun invoke(model: RegisterData): Result<String> =
        authService.register(model = model)
}
