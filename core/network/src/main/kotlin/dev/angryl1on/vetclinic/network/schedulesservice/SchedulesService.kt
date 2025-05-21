package dev.angryl1on.vetclinic.network.schedulesservice

import dev.angryl1on.vetclinic.model.schedule.ScheduleResponse
import dev.angryl1on.vetclinic.network.extensions.request
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Сервис для работы с расписанием врачей.
 */
interface SchedulesService {

    /**
     * Получает расписание работы врача по его идентификатору.
     *
     * @param token JWT токен для авторизации пользователя.
     * @param doctorId Уникальный идентификатор врача, для которого требуется получить расписание.
     * @return [Result], содержащий список [ScheduleResponse] при успешном выполнении запроса
     * или ошибку в случае неудачи.
     */
    suspend fun getScheduleByDoctorId(token: String, doctorId: Long): Result<List<ScheduleResponse>>
}

class KtorSchedulesService(
    private val client: HttpClient,
    private val apiHost: String,
    private val dispatcher: CoroutineDispatcher
) : SchedulesService {
    override suspend fun getScheduleByDoctorId(
        token: String,
        doctorId: Long
    ): Result<List<ScheduleResponse>> = withContext(dispatcher) {
        client.request<List<ScheduleResponse>> {
            get {
                url {
                    protocol = URLProtocol.HTTP
                    host = apiHost
                    port = 8080
                    path("api", "schedules", "doctor", "$doctorId")
                }
                bearerAuth(token)
            }
        }
    }
}
