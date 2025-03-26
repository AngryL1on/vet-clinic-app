package dev.angryl1on.vetclinic.network.di

import dev.angryl1on.vetclinic.network.BuildConfig
import org.koin.core.qualifier.named
import org.koin.dsl.module

val provideNetworkEndpointsModule = module {
    single(named("API")) {
        BuildConfig.WEB_HOST
    }
}
