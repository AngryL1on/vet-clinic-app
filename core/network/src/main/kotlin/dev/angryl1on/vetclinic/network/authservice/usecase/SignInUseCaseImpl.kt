package dev.angryl1on.vetclinic.network.authservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.authservice.SignInUseCase
import dev.angryl1on.vetclinic.model.auth.AuthData
import dev.angryl1on.vetclinic.model.auth.AuthNetworkResponse
import dev.angryl1on.vetclinic.network.authservice.AuthService

class SignInUseCaseImpl(
    private val authService: AuthService
) : SignInUseCase {

    override suspend fun invoke(model: AuthData): Result<AuthNetworkResponse> =
        authService.signIn(model)
}
