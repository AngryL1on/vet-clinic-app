package dev.angryl1on.vetclinic.network.medicalrecordservice

import dev.angryl1on.vetclinic.model.medicalrecord.MedicalRecord
import dev.angryl1on.vetclinic.network.extensions.request
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Сервис для получения медицинских записей (визитов).
 */
interface MedicalRecordService {

    /**
     * Запрашивает список медицинских записей для указанного питомца.
     *
     * @param token Токен аутентификации, необходимый для авторизации запроса.
     * @param id Уникальный идентификатор питомца, для которого запрашиваются записи.
     * @return [Result] с списком [MedicalRecord] в случае успешного ответа или с ошибкой в случае отказа.
     */
    suspend fun getById(token: String, id: Long): Result<List<MedicalRecord>>
}

class KtorMedicalRecordService(
    private val client: HttpClient,
    private val apiHost: String,
    private val dispatcher: CoroutineDispatcher,
) : MedicalRecordService {
    override suspend fun getById(
        token: String,
        id: Long
    ) = withContext(dispatcher) {
        client.request<List<MedicalRecord>> {
            get {
                url {
                    protocol = URLProtocol.HTTP
                    host = apiHost
                    port = 8080
                    path("api", "medical-records", "by-pet", "$id")
                }
                bearerAuth(token = token)
            }
        }
    }
}
