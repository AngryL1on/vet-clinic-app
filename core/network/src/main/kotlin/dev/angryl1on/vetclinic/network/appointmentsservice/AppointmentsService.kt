package dev.angryl1on.vetclinic.network.appointmentsservice

import dev.angryl1on.vetclinic.model.appointment.AppointmentRequest
import dev.angryl1on.vetclinic.model.appointment.AppointmentResponse
import dev.angryl1on.vetclinic.network.extensions.request
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

interface AppointmentsService {
    suspend fun createAppointment(token: String, model: AppointmentRequest): Boolean

    suspend fun getAvailableSlots(
        token: String,
        doctorId: Long,
        date: String,
        type: String
    ): Result<List<String>>

    suspend fun appointmentByScheduled(token: String): Result<List<AppointmentResponse>>

    suspend fun cancelAppointment(token: String, appointmentId: Long): Result<Unit>
}

class KtorAppointmentsService(
    private val client: HttpClient,
    private val apiHost: String,
    private val dispatcher: CoroutineDispatcher
) : AppointmentsService {
    override suspend fun createAppointment(
        token: String,
        model: AppointmentRequest
    ): Boolean = withContext(dispatcher) {
        val response: HttpResponse = client.post {
            url {
                protocol = URLProtocol.HTTP
                host = apiHost
                port = 8080
                path("api", "appointments")
            }
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(model)
        }

        return@withContext try {
            response.body()
        } catch (e: NoTransformationFoundException) {
            if (response.status == HttpStatusCode.Created) {
                true
            } else {
                Timber.e(e, "Ошибка при десериализации ответа")
                throw e
            }
        }
    }

    override suspend fun getAvailableSlots(
        token: String,
        doctorId: Long,
        date: String,
        type: String
    ): Result<List<String>> = withContext(dispatcher) {
        try {
            val response: List<String> = client.get {
                url {
                    protocol = URLProtocol.HTTP
                    host = apiHost
                    port = 8080
                    path("api", "appointments", "available-slots")
                    parameters.append("doctorId", doctorId.toString())
                    parameters.append("date", date)
                    parameters.append("type", type)
                }
                bearerAuth(token)
            }.body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun appointmentByScheduled(token: String) = withContext(dispatcher) {
        client.request<List<AppointmentResponse>> {
            get {
                url {
                    protocol = URLProtocol.HTTP
                    host = apiHost
                    port = 8080
                    path("api", "appointments", "owner", "SCHEDULED")
                }
                bearerAuth(token)
            }
        }
    }

    override suspend fun cancelAppointment(token: String, appointmentId: Long): Result<Unit> =
        withContext(dispatcher) {
            runCatching {
                val response: HttpResponse = client.put {
                    url {
                        protocol = URLProtocol.HTTP
                        host = apiHost
                        port = 8080
                        path("api", "appointments", "cancel", "$appointmentId")
                    }
                    bearerAuth(token)
                }
                Result.success(Unit)
            }.getOrElse {
                Result.failure(it)
            }
        }

}
