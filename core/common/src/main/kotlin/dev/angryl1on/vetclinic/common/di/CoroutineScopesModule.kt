package dev.angryl1on.vetclinic.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

val provideCoroutineScopesModule = module {
    single {
        val coroutineDispatcher = get<CoroutineDispatcher>(named(VcDispatchers.DEFAULT.name))
        CoroutineScope(SupervisorJob() + coroutineDispatcher)
    }
}
