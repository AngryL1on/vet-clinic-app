package dev.angryl1on.vetclinic

import android.app.Application
import dev.angryl1on.history.di.provideHistoryModule
import dev.angryl1on.profile.di.provideProfileModule
import dev.angryl1on.vetclinic.auth.di.provideAuthModule
import dev.angryl1on.vetclinic.common.di.provideCoroutineScopesModule
import dev.angryl1on.vetclinic.common.di.provideDispatcherModule
import dev.angryl1on.vetclinic.common.di.provideValidationModule
import dev.angryl1on.vetclinic.data.di.provideDataStoreModule
import dev.angryl1on.vetclinic.database.di.provideDatabaseModule
import dev.angryl1on.vetclinic.di.provideNavigationModule
import dev.angryl1on.vetclinic.di.provideViewModelModule
import dev.angryl1on.vetclinic.network.di.provideNetworkEndpointsModule
import dev.angryl1on.vetclinic.network.di.provideNetworkModule
import dev.angryl1on.vetclinic.reporting.ConsoleLoggingTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class VetClinicApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(ConsoleLoggingTree())

        startKoin {
            androidLogger()
            androidContext(this@VetClinicApp)

            // :app modules
            modules(provideNavigationModule, provideViewModelModule)

            // core:common modules
            modules(provideCoroutineScopesModule, provideDispatcherModule, provideValidationModule)

            // core:data modules
            modules(provideDataStoreModule)

            // core:database modules
            modules(provideDatabaseModule)

            // core:network modules
            modules(provideNetworkModule, provideNetworkEndpointsModule)

            // feature:ui-auth modules
            modules(provideAuthModule)

            // feature:ui-history modules
            modules(provideHistoryModule)

            // feature:ui-profile modules
            modules(provideProfileModule)
        }
    }
}
