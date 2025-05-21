package dev.angryl1on.vetclinic.network.branchesservice

import dev.angryl1on.vetclinic.model.branches.BranchResponse
import dev.angryl1on.vetclinic.network.extensions.request
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Сервис для получения информации о филиалах ветеринарной клиники.
 */
interface BranchesService {

    /**
     * Получает список филиалов, предоставляющих услугу с указанным названием.
     *
     * @param token JWT токен авторизации пользователя.
     * @param serviceName Название услуги, по которому производится фильтрация филиалов.
     * @return [Result] содержащий список [BranchResponse] при успешном выполнении запроса,
     * либо ошибку при неудаче.
     */
    suspend fun getByServiceName(token: String, serviceName: String): Result<List<BranchResponse>>
}

class KtorBranchesService(
    private val client: HttpClient,
    private val apiHost: String,
    private val dispatcher: CoroutineDispatcher
) : BranchesService {
    override suspend fun getByServiceName(
        token: String,
        serviceName: String
    ) = withContext(dispatcher) {
        client.request<List<BranchResponse>> {
            get {
                url {
                    protocol = URLProtocol.HTTP
                    host = apiHost
                    port = 8080
                    path("api", "branches", "by-service", serviceName)
                }
                bearerAuth(token)
            }
        }
    }
}
