package dev.angryl1on.vetclinic.domain.usecase.authservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.auth.AuthData
import dev.angryl1on.vetclinic.model.auth.AuthNetworkResponse

interface SignInUseCase : UseCase {
    suspend operator fun invoke(model: AuthData): Result<AuthNetworkResponse>
}
