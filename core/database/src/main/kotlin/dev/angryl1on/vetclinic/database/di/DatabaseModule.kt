package dev.angryl1on.vetclinic.database.di

import androidx.room.Room
import dev.angryl1on.vetclinic.database.VetClinicDatabase
import dev.angryl1on.vetclinic.database.dao.PetDao
import dev.angryl1on.vetclinic.database.usecase.ObservePetsUseCaseImpl
import dev.angryl1on.vetclinic.domain.usecase.petservice.ObservePetsUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val provideDatabaseModule = module {
    single {
        Room
            .databaseBuilder(
                androidContext(),
                VetClinicDatabase::class.java,
                "vetclinic.db"
            )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    single<PetDao> {
        get<VetClinicDatabase>().getPetDao()
    }

    single<ObservePetsUseCase> {
        ObservePetsUseCaseImpl(
            petDao = get()
        )
    }
}
