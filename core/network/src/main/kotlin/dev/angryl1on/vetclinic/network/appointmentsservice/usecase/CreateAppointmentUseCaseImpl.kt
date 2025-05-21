package dev.angryl1on.vetclinic.network.appointmentsservice.usecase

import android.os.Build
import androidx.annotation.RequiresExtension
import dev.angryl1on.vetclinic.domain.usecase.appointmentsservice.CreateAppointmentUseCase
import dev.angryl1on.vetclinic.model.appointment.AppointmentRequest
import dev.angryl1on.vetclinic.network.appointmentsservice.AppointmentsService
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateAppointmentUseCaseImpl(
    private val appointmentsService: AppointmentsService,
    private val tokenSupport: TokenSupport
) : CreateAppointmentUseCase {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun invoke(model: AppointmentRequest): Result<Boolean> =
        withContext(Dispatchers.IO) {
            tokenSupport.withTokenCheck { token ->
                runCatching {
                    appointmentsService.createAppointment(token = token, model = model)
                }
            }
        }
}
