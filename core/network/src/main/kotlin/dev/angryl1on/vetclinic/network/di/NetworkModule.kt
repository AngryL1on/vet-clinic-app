package dev.angryl1on.vetclinic.network.di

import dev.angryl1on.vetclinic.common.di.VcDispatchers
import dev.angryl1on.vetclinic.domain.usecase.authservice.GetUserInfoUseCase
import dev.angryl1on.vetclinic.domain.usecase.authservice.RefreshTokenUseCase
import dev.angryl1on.vetclinic.domain.usecase.authservice.SignInUseCase
import dev.angryl1on.vetclinic.network.authservice.AuthService
import dev.angryl1on.vetclinic.network.authservice.KtorAuthService
import dev.angryl1on.vetclinic.network.authservice.usecase.GetUserInfoUseCaseImpl
import dev.angryl1on.vetclinic.network.authservice.usecase.RefreshTokenUseCaseImpl
import dev.angryl1on.vetclinic.network.authservice.usecase.SignInUseCaseImpl
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupportImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

val provideNetworkModule = module {
    single(named("network")) {
        Json { ignoreUnknownKeys = true }
    }

    single {
        OkHttp.create()
    }

    single {
        val json = get<Json>(named("network"))
        val engine = get<HttpClientEngine>()

        HttpClient(engine) {
            install(ContentNegotiation) { json(json) }

            install(HttpTimeout) {
                connectTimeoutMillis = 20.seconds.inWholeMilliseconds
                requestTimeoutMillis = 60.seconds.inWholeMilliseconds
                socketTimeoutMillis = 20.seconds.inWholeMilliseconds
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }
        }
    }

    single<TokenSupport> {
        TokenSupportImpl(
            userAuthDataStore = get(),
            refreshTokenUseCase = get()
        )
    }

    single<AuthService> {
        KtorAuthService(
            client = get(),
            apiHost = get(named("API")),
            dispatcher = get(named(VcDispatchers.IO.name)),
            authenticationDataStore = get()
        )
    }

    single<SignInUseCase> {
        SignInUseCaseImpl(authService = get())
    }

    single<RefreshTokenUseCase> {
        RefreshTokenUseCaseImpl(authService = get())
    }

    single<GetUserInfoUseCase> {
        GetUserInfoUseCaseImpl(
            authService = get(),
            tokenSupport = get()
        )
    }
}
