package com.farmer.chaitrack.di

import android.content.Context
import androidx.room.Room
import com.farmer.chaitrack.data.local.database.ChaiTrackDatabase
import com.farmer.chaitrack.data.local.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideChaiTrackDatabase(
        @ApplicationContext context: Context
    ): ChaiTrackDatabase {
        return Room.databaseBuilder(
            context,
            ChaiTrackDatabase::class.java,
            "chaitrack_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDeliveryDao(database: ChaiTrackDatabase): DeliveryDao {
        return database.deliveryDao()
    }

    @Provides
    @Singleton
    fun provideFertilizerDao(database: ChaiTrackDatabase): FertilizerDao {
        return database.fertilizerDao()
    }

    @Provides
    @Singleton
    fun provideFinanceDao(database: ChaiTrackDatabase): FinanceDao {
        return database.financeDao()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(database: ChaiTrackDatabase): NotificationDao {
        return database.notificationDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: ChaiTrackDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideAnnouncementDao(database: ChaiTrackDatabase): AnnouncementDao {
        return database.announcementDao()
    }
}