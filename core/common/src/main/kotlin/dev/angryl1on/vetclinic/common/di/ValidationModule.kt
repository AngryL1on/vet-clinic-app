package dev.angryl1on.vetclinic.common.di

import dev.angryl1on.vetclinic.common.presentation.validation.CapitalCharValidator
import dev.angryl1on.vetclinic.common.presentation.validation.EmailValidator
import dev.angryl1on.vetclinic.common.presentation.validation.LatinAlphabetValidator
import dev.angryl1on.vetclinic.common.presentation.validation.PasswordLengthValidator
import org.koin.dsl.module

val provideValidationModule = module {
    single {
        CapitalCharValidator()
    }

    single {
        EmailValidator()
    }

    single {
        LatinAlphabetValidator()
    }

    single {
        PasswordLengthValidator()
    }
}
