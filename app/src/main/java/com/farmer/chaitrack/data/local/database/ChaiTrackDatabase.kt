package com.farmer.chaitrack.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.farmer.chaitrack.data.local.database.dao.*
import com.farmer.chaitrack.data.local.entity.*
import com.farmer.chaitrack.domain.model.FertilizerApplication

@Database(
    entities = [
        UserEntity::class,
        DeliveryEntity::class,
        FertilizerEntity::class,
        FinanceEntity::class,
        NotificationEntity::class,
        AnnouncementEntity::class
    ],
    version = 10,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ChaiTrackDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun deliveryDao(): DeliveryDao
    abstract fun fertilizerDao(): FertilizerDao
    abstract fun financeDao(): FinanceDao
    abstract fun notificationDao(): NotificationDao
    abstract fun announcementDao(): AnnouncementDao

    companion object {
        @Volatile
        private var INSTANCE: ChaiTrackDatabase? = null

        private const val DATABASE_NAME = "chaitrack_database"

        fun getDatabase(context: Context): ChaiTrackDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChaiTrackDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun clearAllTables(context: Context) {
            getDatabase(context).apply {
                //userDao().deleteAllUsers()
                deliveryDao().deleteAllDeliveries()
                fertilizerDao().deleteAllApplications()
                financeDao().deleteAllFinanceRecords()
                notificationDao().deleteAllNotifications()
                announcementDao().deleteAllAnnouncements()
            }
        }
    }
}

class Converters {

    @androidx.room.TypeConverter
    fun fromTimestamp(value: Long?): java.util.Date? {
        return value?.let { java.util.Date(it) }
    }

    @androidx.room.TypeConverter
    fun dateToTimestamp(date: java.util.Date?): Long? {
        return date?.time
    }
}