package dev.angryl1on.profile.di

import dev.angryl1on.profile.presentation.viewmodels.AddPetViewModel
import dev.angryl1on.profile.presentation.viewmodels.EditPetViewModel
import dev.angryl1on.profile.presentation.viewmodels.PetisiansManagementViewModel
import dev.angryl1on.profile.presentation.viewmodels.ProfileViewModel
import dev.angryl1on.vetclinic.model.pet.PetResponse
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val provideProfileModule = module {
    viewModel {
        ProfileViewModel(
            getUserInfoUseCase = get(),
        )
    }

    viewModel {
        PetisiansManagementViewModel(
            getAllPetsUseCase = get(),
            observePetsUseCase = get(),
            deletePetUseCase = get()
        )
    }

    viewModel {
        AddPetViewModel(
            createPetUseCase = get()
        )
    }

    viewModel { (pet: PetResponse) ->
        EditPetViewModel(
            initialPet = pet,
            editPetUseCase = get(),
            uploadPhotoUseCase = get()       // ← сюда Koin должен уметь заинжектить ваш UploadPhotoUseCaseImpl
        )
    }
}
