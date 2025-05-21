package dev.angryl1on.vetclinic.network.di

import dev.angryl1on.vetclinic.common.di.VcDispatchers
import dev.angryl1on.vetclinic.domain.usecase.appointmentsservice.CancelAppointmentUseCase
import dev.angryl1on.vetclinic.domain.usecase.appointmentsservice.CreateAppointmentUseCase
import dev.angryl1on.vetclinic.domain.usecase.appointmentsservice.GetAppointmentByScheduled
import dev.angryl1on.vetclinic.domain.usecase.appointmentsservice.GetAvailableSlotsUseCase
import dev.angryl1on.vetclinic.domain.usecase.authservice.GetUserInfoUseCase
import dev.angryl1on.vetclinic.domain.usecase.authservice.RefreshTokenUseCase
import dev.angryl1on.vetclinic.domain.usecase.authservice.RegisterUseCase
import dev.angryl1on.vetclinic.domain.usecase.authservice.SignInUseCase
import dev.angryl1on.vetclinic.domain.usecase.authservice.VerifyUseCase
import dev.angryl1on.vetclinic.domain.usecase.branchesservice.GetBranchByServiceNameUseCase
import dev.angryl1on.vetclinic.domain.usecase.medicalrecordservice.GetMedicalRecordByPetIdUseCase
import dev.angryl1on.vetclinic.domain.usecase.petservice.CreatePetUseCase
import dev.angryl1on.vetclinic.domain.usecase.petservice.DeletePetUseCase
import dev.angryl1on.vetclinic.domain.usecase.petservice.EditPetUseCase
import dev.angryl1on.vetclinic.domain.usecase.petservice.GetAllPetsUseCase
import dev.angryl1on.vetclinic.domain.usecase.petservice.UploadPhotoUseCase
import dev.angryl1on.vetclinic.domain.usecase.schedulesservice.GetScheduleByDoctorIdUseCase
import dev.angryl1on.vetclinic.domain.usecase.userservice.GetDoctorsByBranchIdUseCase
import dev.angryl1on.vetclinic.network.appointmentsservice.AppointmentsService
import dev.angryl1on.vetclinic.network.appointmentsservice.KtorAppointmentsService
import dev.angryl1on.vetclinic.network.appointmentsservice.usecase.CancelAppointmentUseCaseImpl
import dev.angryl1on.vetclinic.network.appointmentsservice.usecase.CreateAppointmentUseCaseImpl
import dev.angryl1on.vetclinic.network.appointmentsservice.usecase.GetAppointmentByScheduledImpl
import dev.angryl1on.vetclinic.network.appointmentsservice.usecase.GetAvailableSlotsUseCaseImpl
import dev.angryl1on.vetclinic.network.authservice.AuthService
import dev.angryl1on.vetclinic.network.authservice.KtorAuthService
import dev.angryl1on.vetclinic.network.authservice.usecase.GetUserInfoUseCaseImpl
import dev.angryl1on.vetclinic.network.authservice.usecase.RefreshTokenUseCaseImpl
import dev.angryl1on.vetclinic.network.authservice.usecase.RegisterUseCaseImpl
import dev.angryl1on.vetclinic.network.authservice.usecase.SignInUseCaseImpl
import dev.angryl1on.vetclinic.network.authservice.usecase.VerifyUseCaseImpl
import dev.angryl1on.vetclinic.network.branchesservice.BranchesService
import dev.angryl1on.vetclinic.network.branchesservice.KtorBranchesService
import dev.angryl1on.vetclinic.network.branchesservice.usecase.GetBranchByServiceNameUseCaseImpl
import dev.angryl1on.vetclinic.network.medicalrecordservice.KtorMedicalRecordService
import dev.angryl1on.vetclinic.network.medicalrecordservice.MedicalRecordService
import dev.angryl1on.vetclinic.network.medicalrecordservice.usecase.GetMedicalRecordByPetIdUseCaseImpl
import dev.angryl1on.vetclinic.network.petservice.KtorPetService
import dev.angryl1on.vetclinic.network.petservice.PetService
import dev.angryl1on.vetclinic.network.petservice.usecase.CreatePetUseCaseImpl
import dev.angryl1on.vetclinic.network.petservice.usecase.DeletePetUseCaseImpl
import dev.angryl1on.vetclinic.network.petservice.usecase.EditPetUseCaseImpl
import dev.angryl1on.vetclinic.network.petservice.usecase.GetAllPetsUseCaseImpl
import dev.angryl1on.vetclinic.network.petservice.usecase.UploadPhotoUseCaseImpl
import dev.angryl1on.vetclinic.network.schedulesservice.KtorSchedulesService
import dev.angryl1on.vetclinic.network.schedulesservice.SchedulesService
import dev.angryl1on.vetclinic.network.schedulesservice.usecase.GetScheduleByDoctorIdUseCaseImpl
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupportImpl
import dev.angryl1on.vetclinic.network.userservice.KtorUserService
import dev.angryl1on.vetclinic.network.userservice.UserService
import dev.angryl1on.vetclinic.network.userservice.usecase.GetDoctorsByBranchIdUseCaseImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

val provideNetworkModule = module {
    single(named("network")) {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
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
                logger = Logger.SIMPLE
                level = LogLevel.HEADERS
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

    single<PetService> {
        KtorPetService(
            client = get(),
            apiHost = get(named("API")),
            dispatcher = get(named(VcDispatchers.IO.name)),
            petDao = get()
        )
    }

    single<BranchesService> {
        KtorBranchesService(
            client = get(),
            apiHost = get(named("API")),
            dispatcher = get(named(VcDispatchers.IO.name))
        )
    }

    single<MedicalRecordService> {
        KtorMedicalRecordService(
            client = get(),
            apiHost = get(named("API")),
            dispatcher = get(named(VcDispatchers.IO.name))
        )
    }

    single<UserService> {
        KtorUserService(
            client = get(),
            apiHost = get(named("API")),
            dispatcher = get(named(VcDispatchers.IO.name))
        )
    }

    single<SchedulesService> {
        KtorSchedulesService(
            client = get(),
            apiHost = get(named("API")),
            dispatcher = get(named(VcDispatchers.IO.name))
        )
    }

    single<AppointmentsService> {
        KtorAppointmentsService(
            client = get(),
            apiHost = get(named("API")),
            dispatcher = get(named(VcDispatchers.IO.name))
        )
    }

    single<SignInUseCase> {
        SignInUseCaseImpl(authService = get())
    }

    single<RegisterUseCase> {
        RegisterUseCaseImpl(authService = get())
    }

    single<VerifyUseCase> {
        VerifyUseCaseImpl(authService = get())
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

    single<CreatePetUseCase> {
        CreatePetUseCaseImpl(
            petService = get(),
            tokenSupport = get()
        )
    }

    single<GetAllPetsUseCase> {
        GetAllPetsUseCaseImpl(
            petService = get(),
            petDao = get(),
            tokenSupport = get()
        )
    }

    single<EditPetUseCase> {
        EditPetUseCaseImpl(
            petService = get(),
            tokenSupport = get()
        )
    }

    single<UploadPhotoUseCase> {
        UploadPhotoUseCaseImpl(
            petService = get(),
            tokenSupport = get()
        )
    }

    single<DeletePetUseCase> {
        DeletePetUseCaseImpl(
            petService = get(),
            petDao = get(),
            tokenSupport = get()
        )
    }

    single<GetMedicalRecordByPetIdUseCase> {
        GetMedicalRecordByPetIdUseCaseImpl(
            medicalRecordService = get(),
            tokenSupport = get()
        )
    }

    single<GetBranchByServiceNameUseCase> {
        GetBranchByServiceNameUseCaseImpl(
            branchesService = get(),
            tokenSupport = get()
        )
    }

    single<GetDoctorsByBranchIdUseCase> {
        GetDoctorsByBranchIdUseCaseImpl(
            userService = get(),
            tokenSupport = get()
        )
    }

    single<GetScheduleByDoctorIdUseCase> {
        GetScheduleByDoctorIdUseCaseImpl(
            schedulesService = get(),
            tokenSupport = get()
        )
    }

    single<GetAvailableSlotsUseCase> {
        GetAvailableSlotsUseCaseImpl(
            appointmentsService = get(),
            tokenSupport = get()
        )
    }

    single<CreateAppointmentUseCase> {
        CreateAppointmentUseCaseImpl(
            appointmentsService = get(),
            tokenSupport = get()
        )
    }

    single<GetAppointmentByScheduled> {
        GetAppointmentByScheduledImpl(
            appointmentsService = get(),
            tokenSupport = get()
        )
    }

    single<CancelAppointmentUseCase> {
        CancelAppointmentUseCaseImpl(
            appointmentsService = get(),
            tokenSupport = get()
        )
    }
}
