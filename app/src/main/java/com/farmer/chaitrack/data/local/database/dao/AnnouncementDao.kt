package com.farmer.chaitrack.data.local.database.dao

import androidx.room.*
import com.farmer.chaitrack.data.local.entity.AnnouncementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Query("SELECT * FROM announcements ORDER BY date DESC")
    fun getAllAnnouncements(): Flow<List<AnnouncementEntity>>

    @Query("SELECT * FROM announcements WHERE id = :announcementId")
    suspend fun getAnnouncementById(announcementId: String): AnnouncementEntity?

    @Query("""
        SELECT * FROM announcements 
        WHERE date >= :startDate AND date <= :endDate 
        ORDER BY date DESC
    """)
    fun getAnnouncementsByDateRange(startDate: Long, endDate: Long): Flow<List<AnnouncementEntity>>

    @Query("""
        SELECT * FROM announcements 
        WHERE strftime('%Y', datetime(date/1000, 'unixepoch')) = :year
        ORDER BY date DESC
    """)
    fun getAnnouncementsByYear(year: String): Flow<List<AnnouncementEntity>>

    @Query("""
        SELECT * FROM announcements 
        WHERE strftime('%Y-%m', datetime(date/1000, 'unixepoch')) = :yearMonth
        ORDER BY date DESC
    """)
    fun getAnnouncementsByMonth(yearMonth: String): Flow<List<AnnouncementEntity>>

    @Query("""
        SELECT * FROM announcements 
        WHERE isRead = 0 
        ORDER BY date DESC
    """)
    fun getUnreadAnnouncements(): Flow<List<AnnouncementEntity>>

    @Query("""
        SELECT * FROM announcements 
        WHERE isRead = 1 
        ORDER BY date DESC
    """)
    fun getReadAnnouncements(): Flow<List<AnnouncementEntity>>

    @Query("""
        SELECT * FROM announcements 
        WHERE isPinned = 1 
        ORDER BY date DESC
    """)
    fun getPinnedAnnouncements(): Flow<List<AnnouncementEntity>>

    @Query("""
        SELECT * FROM announcements 
        WHERE title LIKE '%' || :searchQuery || '%' 
        OR body LIKE '%' || :searchQuery || '%'
        ORDER BY date DESC
    """)
    fun searchAnnouncements(searchQuery: String): Flow<List<AnnouncementEntity>>

    @Query("SELECT COUNT(*) FROM announcements WHERE isRead = 0")
    fun getUnreadAnnouncementCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM announcements WHERE isRead = 0")
    suspend fun getUnreadAnnouncementCountOnce(): Int

    @Query("""
        SELECT * FROM announcements 
        WHERE date >= :since 
        ORDER BY date DESC
    """)
    fun getAnnouncementsSince(since: Long): Flow<List<AnnouncementEntity>>

    @Query("""
        SELECT * FROM announcements 
        ORDER BY date DESC 
        LIMIT :limit
    """)
    fun getRecentAnnouncements(limit: Int): Flow<List<AnnouncementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: AnnouncementEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(announcements: List<AnnouncementEntity>)

    @Update
    suspend fun updateAnnouncement(announcement: AnnouncementEntity)

    @Query("UPDATE announcements SET isRead = 1 WHERE id = :announcementId")
    suspend fun markAsRead(announcementId: String)

    @Query("UPDATE announcements SET isRead = 1 WHERE isRead = 0")
    suspend fun markAllAsRead()

    @Query("""
        UPDATE announcements 
        SET isRead = 1 
        WHERE id IN (:announcementIds)
    """)
    suspend fun markMultipleAsRead(announcementIds: List<String>)

    @Query("UPDATE announcements SET isPinned = :isPinned WHERE id = :announcementId")
    suspend fun updatePinnedStatus(announcementId: String, isPinned: Boolean)

    @Query("UPDATE announcements SET isPinned = 0")
    suspend fun unpinAllAnnouncements()

    @Delete
    suspend fun deleteAnnouncement(announcement: AnnouncementEntity)

    @Query("DELETE FROM announcements WHERE id = :announcementId")
    suspend fun deleteAnnouncementById(announcementId: String)

    @Query("""
        DELETE FROM announcements 
        WHERE date < :timestamp
    """)
    suspend fun deleteOldAnnouncements(timestamp: Long)

    @Query("""
        DELETE FROM announcements 
        WHERE isRead = 1 AND date < :timestamp AND isPinned = 0
    """)
    suspend fun deleteOldReadAnnouncements(timestamp: Long)

    @Query("DELETE FROM announcements")
    suspend fun deleteAllAnnouncements()

    @Query("SELECT COUNT(*) FROM announcements")
    suspend fun getAnnouncementCount(): Int

    @Query("""
        SELECT COUNT(DISTINCT strftime('%Y-%m', datetime(date/1000, 'unixepoch'))) 
        FROM announcements
    """)
    suspend fun getMonthsWithAnnouncementsCount(): Int
}