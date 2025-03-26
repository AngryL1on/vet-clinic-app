package dev.angryl1on.vetclinic.domain.usecase.authservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.auth.AuthNetworkResponse
import dev.angryl1on.vetclinic.model.auth.RefreshToken

interface RefreshTokenUseCase : UseCase {
    suspend operator fun invoke(model: RefreshToken): Result<AuthNetworkResponse>
}
