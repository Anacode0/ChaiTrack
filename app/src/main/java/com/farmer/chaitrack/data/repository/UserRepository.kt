package com.farmer.chaitrack.data.repository

import com.farmer.chaitrack.data.local.database.dao.UserDao
import com.farmer.chaitrack.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun insertUser(user: UserEntity): Long = withContext(dispatcher) {
        userDao.insert(user)
    }

    suspend fun updateUser(user: UserEntity): Boolean = withContext(dispatcher) {
        userDao.update(user) > 0
    }

    suspend fun deleteUser(user: UserEntity): Boolean = withContext(dispatcher) {
        userDao.delete(user) > 0
    }

    fun observeAllUsers(): Flow<List<UserEntity>> = userDao.observeAllSorted()

    fun observeUser(userId: String): Flow<UserEntity?> = userDao.getCurrentUser()

    suspend fun searchUsers(query: String): List<UserEntity> = withContext(dispatcher) {
        userDao.search(query)
    }

    suspend fun getActiveUsers(): List<UserEntity> = withContext(dispatcher) {
        userDao.observeByActiveStatus(true).first()
    }

    suspend fun deactivateUser(userId: String): Boolean = withContext(dispatcher) {
        userDao.deactivateUser(userId)
    }

    suspend fun getUserById(userId: String): UserEntity? = withContext(dispatcher) {
        userDao.getUserById(userId)
    }

    suspend fun getAllUsers(): List<UserEntity> = withContext(dispatcher) {
        userDao.getAll()
    }

    suspend fun insertOrUpdateUser(user: UserEntity) = withContext(dispatcher) {
        userDao.insertUser(user)
    }

    suspend fun deleteAllUsers() = withContext(dispatcher) {
        userDao.deleteAllUsers()
    }

    suspend fun getUsersCount(): Int = withContext(dispatcher) {
        userDao.getCount()
    }
}