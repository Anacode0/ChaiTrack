package com.farmer.chaitrack.data.local.database.dao

import androidx.room.*
import com.farmer.chaitrack.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE id = :notificationId LIMIT 1")
    suspend fun getNotificationById(notificationId: String): NotificationEntity?

    @Query("""
        SELECT * FROM notifications 
        WHERE isRead = 0 
        ORDER BY timestamp DESC
    """)
    fun getUnreadNotifications(): Flow<List<NotificationEntity>>

    @Query("""
        SELECT * FROM notifications 
        WHERE isRead = 1 
        ORDER BY timestamp DESC
    """)
    fun getReadNotifications(): Flow<List<NotificationEntity>>

    @Query("""
        SELECT * FROM notifications 
        WHERE type = :type 
        ORDER BY timestamp DESC
    """)
    fun getNotificationsByType(type: String): Flow<List<NotificationEntity>>

    @Query("""
        SELECT * FROM notifications 
        WHERE timestamp >= :startTime AND timestamp <= :endTime 
        ORDER BY timestamp DESC
    """)
    fun getNotificationsByTimeRange(startTime: Long, endTime: Long): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadNotificationCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    suspend fun getUnreadNotificationCountOnce(): Int

    @Query("""
        SELECT COUNT(*) FROM notifications 
        WHERE type = :type AND isRead = 0
    """)
    suspend fun getUnreadCountByType(type: String): Int

    @Query("""
        SELECT * FROM notifications 
        WHERE timestamp >= :since 
        ORDER BY timestamp DESC
    """)
    fun getNotificationsSince(since: Long): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Update
    suspend fun updateNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: String)

    @Query("UPDATE notifications SET isRead = 1 WHERE isRead = 0")
    suspend fun markAllAsRead()

    @Query("""
        UPDATE notifications 
        SET isRead = 1 
        WHERE type = :type AND isRead = 0
    """)
    suspend fun markAllAsReadByType(type: String)

    @Query("""
        UPDATE notifications 
        SET isRead = 1 
        WHERE id IN (:notificationIds)
    """)
    suspend fun markMultipleAsRead(notificationIds: List<String>)

    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)

    @Query("DELETE FROM notifications WHERE id = :notificationId")
    suspend fun deleteNotificationById(notificationId: String)

    @Query("""
        DELETE FROM notifications 
        WHERE timestamp < :timestamp
    """)
    suspend fun deleteOldNotifications(timestamp: Long)

    @Query("""
        DELETE FROM notifications 
        WHERE isRead = 1 AND timestamp < :timestamp
    """)
    suspend fun deleteOldReadNotifications(timestamp: Long)

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()

    @Query("SELECT COUNT(*) FROM notifications")
    suspend fun getNotificationCount(): Int

    @Query("""
        SELECT * FROM notifications 
        ORDER BY timestamp DESC 
        LIMIT :limit
    """)
    fun getRecentNotifications(limit: Int): Flow<List<NotificationEntity>>

    @Query("UPDATE notifications SET isRead = 0 WHERE id = :notificationId")
    suspend fun markAsUnread(notificationId: String)

    @Query("DELETE FROM notifications WHERE id IN (:notificationIds)")
    suspend fun deleteMultipleNotifications(notificationIds: List<String>)
}