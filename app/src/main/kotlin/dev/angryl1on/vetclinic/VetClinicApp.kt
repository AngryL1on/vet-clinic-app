package dev.angryl1on.vetclinic

import android.app.Application
import dev.angryl1on.vetclinic.di.provideNavigationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class VetClinicApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@VetClinicApp)

            // :app modules
            modules(provideNavigationModule)
        }
    }
}
