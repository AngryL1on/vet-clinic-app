package dev.angryl1on.main.di

import dev.angryl1on.main.presentation.viewmodel.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val provideMainModule = module {
    viewModel {
        MainViewModel(
            getAppointmentByScheduled = get(),
            cancelAppointmentUseCase = get()
        )
    }
}
