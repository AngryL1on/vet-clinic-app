package dev.angryl1on.vetclinic.di

import dev.angryl1on.vetclinic.ui.presentation.viewmodel.MainActivityViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val provideViewModelModule = module {
    viewModel {
        MainActivityViewModel(
            getCurrentUserUseCase = get(),
            authDataStore = get()
        )
    }
}
