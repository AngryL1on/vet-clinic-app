package dev.angryl1on.appointment.di

import dev.angryl1on.appointment.presentation.viewmodels.AppointmentViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val provideAppointmentModule = module {
    viewModel {
        AppointmentViewModel(
            getAllPetsUseCase = get(),
            getBranchByServiceNameUseCase = get(),
            getDoctorsByBranchIdUseCase = get(),
            getScheduleByDoctorIdUseCase = get(),
            getAvailableSlotsUseCase = get(),
            createAppointmentUseCase = get()
        )
    }
}
