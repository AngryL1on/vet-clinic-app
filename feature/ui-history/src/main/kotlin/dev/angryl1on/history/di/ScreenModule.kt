package dev.angryl1on.history.di

import dev.angryl1on.history.presentation.viewmodels.HistoryScreenViewModel
import dev.angryl1on.history.presentation.viewmodels.MedicalHistoryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val provideHistoryModule = module {
    viewModel {
        HistoryScreenViewModel(
            getAllPetsUseCase = get()
        )
    }

    viewModel { (petId: Long) ->
        MedicalHistoryViewModel(petId, getMedicalRecordByPetId = get())
    }
}
