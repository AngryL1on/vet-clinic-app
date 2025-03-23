package dev.angryl1on.vetclinic.di

import dev.angryl1on.vetclinic.ui.R
import dev.angryl1on.vetclinic.ui.components.navigation.BottomNavItemData
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val provideNavigationModule = module {
    single {
        val context = androidContext()

        listOf(
            BottomNavItemData(
                R.drawable.ic_home,
                context.getString(R.string.home)
            ),
            BottomNavItemData(
                R.drawable.ic_date_pick,
                context.getString(R.string.recording)
            ),
            BottomNavItemData(
                R.drawable.ic_history,
                context.getString(R.string.history)
            ),
            BottomNavItemData(
                R.drawable.ic_person,
                context.getString(R.string.profile)
            )
        )
    }
}
