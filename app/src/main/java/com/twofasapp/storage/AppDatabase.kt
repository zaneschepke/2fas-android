package com.twofasapp.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.twofasapp.data.notifications.local.NotificationsDao
import com.twofasapp.data.notifications.local.model.NotificationEntity
import com.twofasapp.data.services.local.ServiceDao
import com.twofasapp.data.services.local.model.ServiceEntity
import com.twofasapp.storage.converter.Converters

@Database(
    entities = [
        ServiceEntity::class,
        NotificationEntity::class,
    ],
    version = AppDatabase.DB_VERSION,
    exportSchema = true,
    autoMigrations = [
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DB_VERSION = 13
    }

    abstract fun serviceDao(): ServiceDao
    abstract fun notificationDao(): NotificationsDao
}