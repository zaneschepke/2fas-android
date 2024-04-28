package com.twofasapp.di

import com.twofasapp.android.navigation.DeeplinkHandler
import com.twofasapp.common.di.KoinModule
import com.twofasapp.migration.ClearObsoletePrefs
import com.twofasapp.migration.MigratePin
import com.twofasapp.migration.MigrateUnknownServices
import com.twofasapp.storage.EncryptedPreferences
import com.twofasapp.storage.PlainPreferences
import com.twofasapp.workmanager.SyncTimeWorkDispatcher
import com.twofasapp.workmanager.SyncTimeWorkDispatcherImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class StartModule : KoinModule {

    override fun provide() = module {
        singleOf(::DeeplinkHandler)

        singleOf(::SyncTimeWorkDispatcherImpl) { bind<SyncTimeWorkDispatcher>() }

        single {
            ClearObsoletePrefs(
                get<PlainPreferences>(),
                get<EncryptedPreferences>(),
            )
        }
        singleOf(::MigratePin)
        singleOf(::MigrateUnknownServices)
    }
}