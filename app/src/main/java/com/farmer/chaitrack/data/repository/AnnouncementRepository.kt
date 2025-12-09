package com.farmer.chaitrack.data.repository

import com.farmer.chaitrack.data.local.database.dao.AnnouncementDao
import com.farmer.chaitrack.data.local.preferences.PreferencesManager
import com.farmer.chaitrack.data.remote.api.AnnouncementApi
import com.farmer.chaitrack.data.remote.dto.Announcement
import com.farmer.chaitrack.di.IoDispatcher
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnnouncementRepository @Inject constructor(
    private val announcementApi: AnnouncementApi,
    private val announcementDao: AnnouncementDao,
    private val preferencesManager: PreferencesManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher)
{

    suspend fun getAnnouncements(page: Int = 1, limit: Int = 20): Result<List<Announcement>> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error("Not authenticated")
                }

                val response = announcementApi.getAnnouncements("Bearer $token", page, limit)

                if (response.isSuccessful) {
                    val announcements = response.body() ?: emptyList()
                    Result.success(announcements)
                } else {
                    Result.error("Failed to fetch announcements: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.error(e.message ?: "Network error occurred", e)
            }
        }
    }

    suspend fun getAnnouncementById(id: String): Result<Announcement> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error("Not authenticated")
                }

                val response = announcementApi.getAnnouncementById("Bearer $token", id)

                if (response.isSuccessful) {
                    val announcement = response.body()
                    if (announcement != null) {
                        Result.success(announcement)
                    } else {
                        Result.error("Announcement not found")
                    }
                } else {
                    Result.error("Failed to fetch announcement: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.error(e.message ?: "Network error occurred", e)
            }
        }
    }

    suspend fun getAnnouncementsByCategory(
        category: String,
        page: Int = 1,
        limit: Int = 20
    ): Result<List<Announcement>> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error("Not authenticated")
                }

                val response = announcementApi.getAnnouncementsByCategory(
                    "Bearer $token",
                    category,
                    page,
                    limit
                )

                if (response.isSuccessful) {
                    val announcements = response.body() ?: emptyList()
                    Result.success(announcements)
                } else {
                    Result.error("Failed to fetch announcements: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.error(e.message ?: "Network error occurred", e)
            }
        }
    }

    suspend fun getUnreadAnnouncementsCount(): Result<Int> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error("Not authenticated")
                }

                val response = announcementApi.getUnreadAnnouncementsCount("Bearer $token")

                if (response.isSuccessful) {
                    val count = response.body() ?: 0
                    Result.success(count)
                } else {
                    Result.error("Failed to fetch unread count: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.error(e.message ?: "Network error occurred", e)
            }
        }
    }

    suspend fun markAsRead(announcementId: String): Result<Unit> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error("Not authenticated")
                }

                val response = announcementApi.markAsRead("Bearer $token", announcementId)

                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.error("Failed to mark as read: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.error(e.message ?: "Network error occurred", e)
            }
        }
    }

    suspend fun markAllAsRead(): Result<Unit> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error("Not authenticated")
                }

                val response = announcementApi.markAllAsRead("Bearer $token")

                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.error("Failed to mark all as read: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.error(e.message ?: "Network error occurred", e)
            }
        }
    }

    suspend fun getUrgentAnnouncements(limit: Int = 5): Result<List<Announcement>> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error("Not authenticated")
                }

                val response = announcementApi.getUrgentAnnouncements("Bearer $token", limit)

                if (response.isSuccessful) {
                    val announcements = response.body() ?: emptyList()
                    Result.success(announcements)
                } else {
                    Result.error("Failed to fetch urgent announcements: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.error(e.message ?: "Network error occurred", e)
            }
        }
    }

    suspend fun searchAnnouncements(
        query: String,
        page: Int = 1,
        limit: Int = 20
    ): Result<List<Announcement>> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error("Not authenticated")
                }

                val response = announcementApi.searchAnnouncements(
                    "Bearer $token",
                    query,
                    page,
                    limit
                )

                if (response.isSuccessful) {
                    val announcements = response.body() ?: emptyList()
                    Result.success(announcements)
                } else {
                    Result.error("Failed to search announcements: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.error(e.message ?: "Network error occurred", e)
            }
        }
    }

    suspend fun getAnnouncementsSince(
        since: String,
        page: Int = 1,
        limit: Int = 50
    ): Result<List<Announcement>> {
        return withContext(dispatcher) {
            try {
                val token = preferencesManager.getAuthToken()
                if (token == null) {
                    return@withContext Result.error("Not authenticated")
                }

                val response = announcementApi.getAnnouncementsSince(
                    "Bearer $token",
                    since,
                    page,
                    limit
                )

                if (response.isSuccessful) {
                    val announcements = response.body() ?: emptyList()
                    Result.success(announcements)
                } else {
                    Result.error("Failed to fetch announcements: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.error(e.message ?: "Network error occurred", e)
            }
        }
    }
}