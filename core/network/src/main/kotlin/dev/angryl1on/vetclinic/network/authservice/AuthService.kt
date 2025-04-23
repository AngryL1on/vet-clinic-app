package dev.angryl1on.vetclinic.network.authservice

import dev.angryl1on.vetclinic.data.auth.AuthenticationDataStore
import dev.angryl1on.vetclinic.model.auth.AuthData
import dev.angryl1on.vetclinic.model.auth.AuthDataStore
import dev.angryl1on.vetclinic.model.auth.AuthNetworkResponse
import dev.angryl1on.vetclinic.model.auth.RefreshToken
import dev.angryl1on.vetclinic.model.auth.UserInfo
import dev.angryl1on.vetclinic.model.auth.asUserAuthDataStore
import dev.angryl1on.vetclinic.network.extensions.request
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Интерфейс для авторизации пользователя
 */
interface AuthService {

    /**
     * Метод авторизации пользователя, используя данные его входа в систему и пароль
     *
     * @property [model] Данные и пароль пользователя. Использует [AuthData] data class
     */
    suspend fun signIn(model: AuthData): Result<AuthNetworkResponse>

    /**
     * Метод обновления токены пользователя, используя его токен обновления
     *
     * @property [model] токен обновления пользователя. Использует [RefreshToken] data class
     */
    suspend fun refreshToken(model: RefreshToken): Result<AuthNetworkResponse>

    /**
     * Метод получения информации пользователя, используя его токен доступа
     *
     * @property [token] токен обновления пользователя. Использует [RefreshToken] data class
     */
    suspend fun getInfo(token: String): Result<UserInfo>
}

class KtorAuthService(
    private val client: HttpClient,
    private val apiHost: String,
    private val dispatcher: CoroutineDispatcher,
    private val authenticationDataStore: AuthenticationDataStore
) : AuthService {
    override suspend fun signIn(model: AuthData) = withContext(dispatcher) {
        client.request<AuthNetworkResponse> {
            post {
                url {
                    protocol = URLProtocol.HTTP
                    host = apiHost
                    port = 8080
                    path("api", "auth", "login")
                    contentType(ContentType.Application.Json)
                }
                setBody(model)
            }
        }.also { response ->
            response.onSuccess { authNetworkResponse ->
                authenticationDataStore.updateUser(authNetworkResponse.asUserAuthDataStore())
            }
        }
    }

    override suspend fun refreshToken(model: RefreshToken) = withContext(dispatcher) {
        client.request<AuthNetworkResponse> {
            post {
                url {
                    protocol = URLProtocol.HTTP
                    host = apiHost
                    port = 8080
                    path("api", "auth", "refresh")
                    contentType(ContentType.Application.Json)
                }
                setBody(model)
            }
        }.also { response ->
            response.onSuccess { authNetworkResponse ->
                authNetworkResponse.asUserAuthDataStore().let { updatedData ->
                    authenticationDataStore.updateUser(
                        AuthDataStore(
                            id = updatedData.id,
                            accessToken = updatedData.accessToken,
                            refreshToken = updatedData.refreshToken,
                            accessTokenExpiresIn = updatedData.accessTokenExpiresIn,
                            refreshTokenExpiresIn = updatedData.refreshTokenExpiresIn
                        )
                    )
                }
            }
        }
    }

    override suspend fun getInfo(token: String) = withContext(dispatcher) {
        client.request<UserInfo> {
            get {
                url {
                    protocol = URLProtocol.HTTP
                    host = apiHost
                    port = 8080
                    path("api", "auth", "user")
                    contentType(ContentType.Application.Json)
                }
                bearerAuth(token = token)
                Timber.d(token)
            }
        }
    }
}
