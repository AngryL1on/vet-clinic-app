package dev.angryl1on.vetclinic.network.authservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.authservice.RefreshTokenUseCase
import dev.angryl1on.vetclinic.model.auth.AuthNetworkResponse
import dev.angryl1on.vetclinic.model.auth.RefreshToken
import dev.angryl1on.vetclinic.network.authservice.AuthService

class RefreshTokenUseCaseImpl(
    private val authService: AuthService
) : RefreshTokenUseCase {

    override suspend fun invoke(model: RefreshToken): Result<AuthNetworkResponse> =
        authService.refreshToken(model)
}
