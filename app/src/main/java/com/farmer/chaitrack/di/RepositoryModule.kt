package com.farmer.chaitrack.di

import com.farmer.chaitrack.data.local.database.dao.*
import com.farmer.chaitrack.data.local.preferences.PreferencesManager
import com.farmer.chaitrack.data.remote.api.*
import com.farmer.chaitrack.data.repository.*
import com.farmer.chaitrack.domain.repository.*
import com.farmer.chaitrack.di.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFertilizerRepository(
        impl: FertilizerRepositoryImpl
    ): FertilizerRepository

    @Binds
    @Singleton
    abstract fun bindFinanceRepository(
        impl: FinanceRepositoryImpl
    ): FinanceRepository

    companion object {
        @Provides
        @Singleton
        fun provideAuthRepository(
            authApi: AuthApi,
            preferencesManager: PreferencesManager
        ): AuthRepository {
            return AuthRepository(
                authApi = authApi,
                preferencesManager = preferencesManager
            )
        }

        @Provides
        @Singleton
        fun provideDeliveryRepository(
            deliveryApi: DeliveryApi,
            deliveryDao: DeliveryDao,
            preferencesManager: PreferencesManager,
            @IoDispatcher dispatcher: CoroutineDispatcher
        ): DeliveryRepository {
            return DeliveryRepository(
                deliveryApi = deliveryApi,
                deliveryDao = deliveryDao,
                preferencesManager = preferencesManager,
                dispatcher = dispatcher
            )
        }

        @Provides
        @Singleton
        fun provideNotificationRepository(
            notificationApi: NotificationApi,
            notificationDao: NotificationDao,
            preferencesManager: PreferencesManager,
            @IoDispatcher dispatcher: CoroutineDispatcher
        ): NotificationRepository {
            return NotificationRepository(
                notificationApi = notificationApi,
                notificationDao,
                preferencesManager = preferencesManager,
                dispatcher = dispatcher
            )
        }

        @Provides
        @Singleton
        fun provideAnnouncementRepository(
            announcementApi: AnnouncementApi,
            announcementDao: AnnouncementDao,
            preferencesManager: PreferencesManager,
            @IoDispatcher dispatcher: CoroutineDispatcher
        ): AnnouncementRepository {
            return AnnouncementRepository(
                announcementApi = announcementApi,
                announcementDao = announcementDao,
                preferencesManager = preferencesManager,
                dispatcher = dispatcher
            )
        }

        @Provides
        @Singleton
        fun provideUserRepository(
            userDao: UserDao,
            @IoDispatcher dispatcher: CoroutineDispatcher
        ): UserRepository {
            return UserRepository(
                userDao = userDao,
                dispatcher = dispatcher
            )
        }
    }
}