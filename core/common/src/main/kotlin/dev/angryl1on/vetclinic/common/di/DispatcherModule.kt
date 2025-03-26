package dev.angryl1on.vetclinic.common.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val provideDispatcherModule = module {
    single(named(VcDispatchers.DEFAULT.name)) {
        Dispatchers.Default
    }

    single(named(VcDispatchers.IO.name)) {
        Dispatchers.IO
    }
}

enum class VcDispatchers {
    IO,
    DEFAULT
}
