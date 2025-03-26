package dev.angryl1on.vetclinic.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dev.angryl1on.vetclinic.data.auth.AuthenticationDataStore
import dev.angryl1on.vetclinic.data.auth.AuthenticationDataStoreImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user-auth")

val provideDataStoreModule = module {
    single {
        val context = androidContext()
        context.dataStore
    }

    single<AuthenticationDataStore> {
        AuthenticationDataStoreImpl(get())
    }
}
