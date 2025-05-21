package dev.angryl1on.vetclinic.network.appointmentsservice.usecase

import android.os.Build
import androidx.annotation.RequiresExtension
import dev.angryl1on.vetclinic.domain.usecase.appointmentsservice.CancelAppointmentUseCase
import dev.angryl1on.vetclinic.network.appointmentsservice.AppointmentsService
import dev.angryl1on.vetclinic.network.tokenservice.TokenSupport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CancelAppointmentUseCaseImpl(
    private val appointmentsService: AppointmentsService,
    private val tokenSupport: TokenSupport
) : CancelAppointmentUseCase {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun invoke(appointmentId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        tokenSupport.withTokenCheck { token ->
            appointmentsService.cancelAppointment(
                token = token,
                appointmentId = appointmentId
            )
        }
    }
}
