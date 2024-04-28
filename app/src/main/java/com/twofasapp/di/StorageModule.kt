package com.twofasapp.di

import androidx.room.Room
import com.twofasapp.common.di.KoinModule
import com.twofasapp.common.environment.AppBuild
import com.twofasapp.storage.AppDatabase
import com.twofasapp.storage.cipher.DatabaseKeyGenerator
import com.twofasapp.storage.cipher.DatabaseKeyGeneratorRandom
import com.twofasapp.storage.cipher.GetDatabaseMasterKey
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class StorageModule : KoinModule {

    override fun provide() = module {
        singleOf(::DatabaseKeyGeneratorRandom) { bind<DatabaseKeyGenerator>() }
        singleOf(::GetDatabaseMasterKey)

        single<AppDatabase> {
            val context = androidContext()

            val builder = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "database-2fas"
            )

            if (get<AppBuild>().debuggable.not()) {
                val factory = SupportFactory(SQLiteDatabase.getBytes(get<GetDatabaseMasterKey>().execute().toCharArray()))
                builder.openHelperFactory(factory)
            }

            builder.build()
        }

        single { get<AppDatabase>().serviceDao() }
        single { get<AppDatabase>().notificationDao() }
    }
}