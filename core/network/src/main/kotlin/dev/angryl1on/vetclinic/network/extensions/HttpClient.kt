package dev.angryl1on.vetclinic.network.extensions

import dev.angryl1on.vetclinic.model.ErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import timber.log.Timber

/**
 * Checks the HTTP response code and handles different status codes accordingly
 *
 * @return a [Result] with the response data of type [T]
 */
internal suspend inline fun <reified T> HttpResponse.handleResponse() = when {
    status.isSuccess() -> {
        Timber.d("Response: %s", this)
        Result.success(value = body<T>())
    }

//    status.isRedirection() -> {
//        Result.failure<T>(
//            exception = ResponseExceptionWithMessage(
//                responseException = RedirectResponseException(this, "Redirection error"),
//                serverMessage = parseErrors()
//            )
//        ).also { result ->
//            Timber.e("Redirection error: ${result.exceptionOrNull()}")
//        }
//    }
//
//    status.isClientError() -> {
//        Result.failure<T>(
//            exception = ResponseExceptionWithMessage(
//                responseException = ClientRequestException(this, "Client error"),
//                serverMessage = parseErrors()
//            )
//        ).also { result ->
//            Timber.e("Client error: ${result.exceptionOrNull()}")
//        }
//    }
//
//    status.isServerError() -> {
//        Result.failure<T>(
//            exception = ResponseExceptionWithMessage(
//                responseException = ServerResponseException(this, "Server error"),
//                serverMessage = parseErrors()
//            )
//        ).also { result ->
//            Timber.e("Server error: ${result.exceptionOrNull()}")
//        }
//    }

    else -> {
        Timber.e("Unknown response status code: $this")
        Result.failure(exception = ResponseException(this, status.description))
    }
}

/**
 * Performs a request and checks the response code for error handling
 *
 * @param [block] the lambda for configuring the HTTP request
 * @return a [Result] with the response data of type [T]
 */
internal suspend inline fun <reified T> HttpClient.request(block: HttpClient.() -> HttpResponse): Result<T> {
    return try {
        block().handleResponse()
    } catch (exception: Exception) {
        Timber.e("Unknown error: $exception")
        Result.failure(exception = exception)
    }
}

/**
 * Parse request response errors
 */
internal suspend inline fun HttpResponse.parseErrors(): ErrorResponse {
    val bodyText = this.bodyAsText()
    return try {
        Json.decodeFromString<ErrorResponse>(bodyText)
    } catch (e: Exception) {
        ErrorResponse(
            status = this.status.value,
            error = "ParsingError",
            message = bodyText.ifBlank { "Неизвестная ошибка" }
        )
    }
}

