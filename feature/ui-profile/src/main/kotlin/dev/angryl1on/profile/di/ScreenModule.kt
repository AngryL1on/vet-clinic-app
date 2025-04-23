package dev.angryl1on.profile.di

import dev.angryl1on.profile.presentation.viewmodels.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val provideProfileModule = module {
    viewModel {
        ProfileViewModel(
            getUserInfoUseCase = get(),
        )
    }
}
