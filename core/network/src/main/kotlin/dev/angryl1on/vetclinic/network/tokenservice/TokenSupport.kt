package dev.angryl1on.vetclinic.network.tokenservice

import dev.angryl1on.vetclinic.data.auth.AuthenticationDataStore
import dev.angryl1on.vetclinic.domain.usecase.authservice.RefreshTokenUseCase
import dev.angryl1on.vetclinic.model.auth.RefreshToken
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.first
import timber.log.Timber

interface TokenSupport {

    /**
     * accompanies a request to validate a token.
     *
     * @property [body] request which will be send.
     * @return [Result] request response which wrapped in Result.
     */
    suspend fun <T> withTokenCheck(body: suspend (accessToken: String) -> Result<T>): Result<T>
}

class TokenSupportImpl(
    private val userAuthDataStore: AuthenticationDataStore,
    private val refreshTokenUseCase: RefreshTokenUseCase
) : TokenSupport {

    override suspend fun <T> withTokenCheck(body: suspend (accessToken: String) -> Result<T>): Result<T> {
        val userAuthDataStore = userAuthDataStore.fetchUsers().first()
        val accessToken = userAuthDataStore.accessToken

        Timber.d("Attempting to get a response")
        val result = body(accessToken ?: "")

        if (accessToken != null) {
            result.onFailure { exception ->
                if (exception is ClientRequestException) {
                    Timber.d("Failed token, starting refresh")
                    userAuthDataStore.refreshToken?.let { refreshToken ->
                        refreshTokenUseCase(model = RefreshToken(refreshToken)).run {
                            onSuccess { response ->
                                Timber.d("Token successfully refreshed")
                                return body(response.accessToken)
                            }
                        }
                    } ?: Timber.d("RefreshToken is null")
                }
            }
        }

        return result
    }
}
