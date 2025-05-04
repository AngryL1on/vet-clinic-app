package dev.angryl1on.vetclinic.domain.usecase.authservice

import dev.angryl1on.vetclinic.domain.usecase.UseCase
import dev.angryl1on.vetclinic.model.auth.UserInfo

interface GetUserInfoUseCase : UseCase {
    suspend operator fun invoke(): Result<UserInfo>
}
