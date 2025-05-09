package dev.angryl1on.vetclinic.auth.di

import androidx.lifecycle.SavedStateHandle
import dev.angryl1on.vetclinic.auth.presentation.viewmodels.LoginViewModel
import dev.angryl1on.vetclinic.auth.presentation.viewmodels.RegisterViewModel
import dev.angryl1on.vetclinic.auth.presentation.viewmodels.SplashScreenViewModel
import dev.angryl1on.vetclinic.auth.presentation.viewmodels.VerifyRegisterViewModel
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

    viewModel {
        RegisterViewModel(
            registerUseCase = get()
        )
    }

    viewModel { (state: SavedStateHandle) ->           // ⬅️  Koin даст SavedStateHandle
        VerifyRegisterViewModel(
            savedStateHandle = state,
            verifyUseCase    = get()
        )
    }
}
