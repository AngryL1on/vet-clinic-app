package dev.angryl1on.vetclinic.testing.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val provideTestDispatcherModule = module {
    single<TestDispatcher> {
        UnconfinedTestDispatcher()
    }

    single<CoroutineDispatcher>(named(VcDispatchers.DEFAULT.name)) {
        get<TestDispatcher>()
    }

    single<CoroutineDispatcher>(named(VcDispatchers.IO.name)) {
        get<TestDispatcher>()
    }
}

enum class VcDispatchers {
    IO,
    DEFAULT
}
