package dev.angryl1on.vetclinic.auth.di

import dev.angryl1on.vetclinic.auth.presentation.viewmodels.LoginViewModel
import dev.angryl1on.vetclinic.auth.presentation.viewmodels.SplashScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val provideAuthModule = module {
    viewModel {
        LoginViewModel(
            signIn = get(),
            emailValidator = get()
        )
    }

    viewModel {
        SplashScreenViewModel(
            refreshTokenUseCase = get(),
            authenticationDataStore = get()
        )
    }
}
