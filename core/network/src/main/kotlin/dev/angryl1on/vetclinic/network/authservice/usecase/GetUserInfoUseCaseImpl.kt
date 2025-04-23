package dev.angryl1on.vetclinic.network.authservice.usecase

import dev.angryl1on.vetclinic.domain.usecase.authservice.GetUserInfoUseCase
import dev.angryl1on.vetclinic.model.auth.UserInfo
import dev.angryl1on.vetclinic.network.authservice.AuthService
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport

class GetUserInfoUseCaseImpl(
    private val authService: AuthService,
    private val tokenSupport: TokenSupport
) : GetUserInfoUseCase {
    override suspend fun invoke(): Result<UserInfo> =
        tokenSupport.withTokenCheck { token ->
            authService.getInfo(token)
        }
}