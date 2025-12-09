package com.farmer.chaitrack.data.local.database.dao

import androidx.room.*
import com.farmer.chaitrack.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll(): List<UserEntity>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getById(userId: String): UserEntity?

    @Insert
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity): Int

    @Delete
    suspend fun delete(user: UserEntity): Int

    @Query("SELECT * FROM users ORDER BY firstName ASC")
    fun observeAllSorted(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE isActive = :isActive")
    fun observeByActiveStatus(isActive: Boolean): Flow<List<UserEntity>>

    @Query("""
        SELECT * FROM users 
        WHERE firstName LIKE '%' || :query || '%' 
           OR lastName LIKE '%' || :query || '%' 
           OR email LIKE '%' || :query || '%'
    """)
    suspend fun search(query: String): List<UserEntity>

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getCount(): Int

    @Query("SELECT * FROM users WHERE createdAt BETWEEN :startTime AND :endTime")
    suspend fun getUsersCreatedBetween(startTime: Long, endTime: Long): List<UserEntity>

    @Insert
    suspend fun insertAll(users: List<UserEntity>): List<Long>

    @Update
    suspend fun updateAll(users: List<UserEntity>): Int

    @Transaction
    suspend fun deactivateUser(userId: String): Boolean {
        val user = getById(userId) ?: return false
        val updatedUser = user.copy(isActive = false)
        return update(updatedUser) > 0
    }

    @Transaction
    suspend fun insertWithTimestamp(user: UserEntity): Long {
        val userWithTime = user.copy(createdAt = System.currentTimeMillis())
        return insert(userWithTime)
    }

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    // @Transaction
    // @Query("SELECT * FROM users WHERE id = :userId")
    // fun getUserWithSettings(userId: String): Flow<UserWithSettings?>

    // @Transaction
    // @Query("SELECT * FROM users")
    // fun getAllUsersWithSettings(): Flow<List<UserWithSettings>>
}