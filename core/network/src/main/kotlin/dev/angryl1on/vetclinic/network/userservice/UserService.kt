package dev.angryl1on.vetclinic.network.userservice

import dev.angryl1on.vetclinic.model.user.DoctorResponseForSelectInAppointment
import dev.angryl1on.vetclinic.network.extensions.request
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Интерфейс для получения информации пользователя
 */
interface UserService {
    suspend fun getDoctorsByBranchId(
        token: String,
        branchId: Long
    ): Result<List<DoctorResponseForSelectInAppointment>>
}

class KtorUserService(
    private val client: HttpClient,
    private val apiHost: String,
    private val dispatcher: CoroutineDispatcher
) : UserService {
    override suspend fun getDoctorsByBranchId(
        token: String,
        branchId: Long
    ) = withContext(dispatcher) {
        client.request<List<DoctorResponseForSelectInAppointment>> {
            get {
                url {
                    protocol = URLProtocol.HTTP
                    host = apiHost
                    port = 8080
                    path("api", "users", "branches", "$branchId", "doctors")
                }
                bearerAuth(token = token)
            }
        }
    }
}
