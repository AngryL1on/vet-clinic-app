package dev.angryl1on.vetclinic.network.petservice

import dev.angryl1on.vetclinic.database.dao.PetDao
import dev.angryl1on.vetclinic.database.table.mapToEntity
import dev.angryl1on.vetclinic.model.pet.PetRequest
import dev.angryl1on.vetclinic.model.pet.PetResponse
import dev.angryl1on.vetclinic.network.extensions.request
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URLConnection


/**
 * Сервис для работы с питомцами через сетевой API.
 * Предоставляет методы для создания, получения, редактирования, удаления питомцев,
 * а также загрузки их фотографий.
 */
interface PetService {

    /**
     * Создает нового питомца.
     *
     * @param token токен авторизации
     * @param model данные питомца для создания
     * @return результат с информацией о созданном питомце
     */
    suspend fun create(token: String, model: PetRequest): Result<PetResponse>

    /**
     * Получает информацию о питомце по его идентификатору.
     *
     * @param token токен авторизации
     * @param id идентификатор питомца
     * @return результат с информацией о питомце
     */
    suspend fun getById(token: String, id: Long): Result<PetResponse>

    /**
     * Получает список всех питомцев, принадлежащих текущему пользователю.
     *
     * @param token токен авторизации
     * @return результат со списком питомцев
     */
    suspend fun getAll(token: String): Result<List<PetResponse>>

    /**
     * Редактирует информацию о питомце.
     *
     * @param token токен авторизации
     * @param id идентификатор редактируемого питомца
     * @param model новые данные питомца
     * @return результат с обновленной информацией о питомце
     */
    suspend fun edit(token: String, id: Long, model: PetRequest): Result<PetResponse>

    /**
     * Загружает фотографию для питомца.
     *
     * @param token токен авторизации
     * @param id идентификатор питомца
     * @param file файл с фотографией
     * @return результат с обновленной информацией о питомце
     */
    suspend fun uploadPhoto(token: String, id: Long, file: File): Result<PetResponse>

    /**
     * Удаляет питомца.
     *
     * @param token токен авторизации
     * @param id идентификатор питомца
     * @return true, если питомец был успешно удален, иначе false
     */
    suspend fun delete(token: String, id: Long): Boolean
}

class KtorPetService(
    private val client: HttpClient,
    private val apiHost: String,
    private val dispatcher: CoroutineDispatcher,
    private val petDao: PetDao
) : PetService {
    override suspend fun create(
        token: String,
        model: PetRequest
    ): Result<PetResponse> = withContext(dispatcher) {
        client.request<PetResponse> {
            post {
                url {
                    protocol = URLProtocol.HTTP
                    host = apiHost
                    port = 8080
                    path("api", "pets")
                    contentType(ContentType.MultiPart.FormData)
                }
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(model)
            }
        }.onSuccess { petDao.insert(it.mapToEntity()) }
    }

    override suspend fun getById(token: String, id: Long) = withContext(dispatcher) {
        client.request<PetResponse> {
            get {
                url {
                    protocol = URLProtocol.HTTP
                    host = apiHost
                    port = 8080
                    path("api", "pets", "$id")
                }
                bearerAuth(token = token)
            }
        }.also { response ->
            response.onSuccess { petNetworkResponse ->
                petDao.insert(petNetworkResponse.mapToEntity())
            }
        }
    }

    override suspend fun getAll(token: String) = withContext(dispatcher) {
        client.request<List<PetResponse>> {
            get {
                url {
                    protocol = URLProtocol.HTTP
                    host = apiHost
                    port = 8080
                    path("api", "pets", "owner")
                }
                bearerAuth(token = token)
            }
        }.also { response ->
            response.onSuccess { listPetNetworkResponse ->
                listPetNetworkResponse.forEach { petList ->
                    petDao.insert(petList.mapToEntity())
                }
            }
        }
    }

    override suspend fun edit(token: String, id: Long, model: PetRequest) =
        withContext(dispatcher) {
            client.request<PetResponse> {
                put {
                    url {
                        protocol = URLProtocol.HTTP
                        host = apiHost
                        port = 8080
                        path("api", "pets", "$id")
                    }
                    contentType(ContentType.Application.Json)
                    bearerAuth(token)
                    setBody(model)
                }
            }.also { response ->
                response.onSuccess { petNetworkResponse ->
                    petDao.insert(petNetworkResponse.mapToEntity())
                }
            }
        }

    override suspend fun uploadPhoto(
        token: String,
        id: Long,
        file: File
    ): Result<PetResponse> = withContext(dispatcher) {
        val mime = URLConnection
            .guessContentTypeFromName(file.name)
            ?: "application/octet-stream"

        client.request<PetResponse> {
            post {
                url {
                    protocol = URLProtocol.HTTP
                    host = apiHost
                    port = 8080
                    path("api", "pets", "$id", "photo")
                    contentType(ContentType.MultiPart.FormData)
                }
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                key = "photo",
                                value = file.readBytes(),
                                headers = Headers.build {
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        """form-data; name="photo"; filename="${file.name}""""
                                    )
                                    append(HttpHeaders.ContentType, mime)
                                }
                            )
                        }
                    ))
            }
//            val err = e.body<ErrorResponse>()
//            throw IllegalStateException("Server error ${err.statusCode}: ${err.message}")
        }.onSuccess { petData -> petDao.insert(petData.mapToEntity()) }
    }

    override suspend fun delete(token: String, id: Long): Boolean = withContext(dispatcher) {
        val response: HttpResponse = client.delete {
            url {
                protocol = URLProtocol.HTTP
                host = apiHost
                port = 8080
                path("api", "pets", "$id")
            }
            bearerAuth(token)
        }

        try {
            response.body<Boolean>()
        } catch (e: NoTransformationFoundException) {
            if (response.status == HttpStatusCode.NoContent) true
            else throw e
        }
    }
}
