package com.example.misisapp.data.repository

import android.util.Log
import com.example.misisapp.data.local.dao.UserDao
import com.example.misisapp.data.mapper.UserMapper
import com.example.misisapp.data.remote.api.UserApi
import com.example.misisapp.data.session.SessionManager
import com.example.misisapp.domain.model.User
import com.example.misisapp.domain.repository.UserRepository
import com.example.misisapp.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userDao: UserDao,
    private val userMapper: UserMapper,
    private val sessionManager: SessionManager
) : UserRepository {

    private var currentUserCache: User? = null
    private var currentUserCacheTime: Long = 0
    private val cacheValidityPeriod = 5 * 60 * 1000L // 5 минут

    override suspend fun getUserByID(id: String): User? {
        // Trying to load user from cache
        val localUser = userDao.getUserById(id)
        if (localUser != null) {
            return userMapper.mapToDomain(localUser)
        }

        // If not, request one by server
        return try {
            val remoteUser = userApi.getUserById(id)
            val entity = userMapper.mapToEntity(remoteUser)
            userDao.insertUser(entity)
            userMapper.mapToDomain(remoteUser)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getCurrentUserProfile(): User? {

        val currentTime = System.currentTimeMillis()
        if (currentUserCache != null && currentTime - currentUserCacheTime < cacheValidityPeriod) {
            return currentUserCache
        }

        return try {
            val userDto = userApi.getUser()
            val user = userMapper.mapToDomain(userDto)

            // Saving user's data in local database
            val userEntity = userMapper.mapToEntity(userDto)
            userDao.insertUser(userEntity)

            currentUserCache = user
            currentUserCacheTime = currentTime

            user
        } catch (e: HttpException) {
            // TODO: catch some expcetions from network
            null
        } catch (e: Exception) {
            Log.e("UserRepImplTAG", e.toString())
            val currentUserId = getCurrentUserId()
            currentUserId?.let { id ->
                userDao.getUserById(id)?.let(userMapper::mapToDomain)
            }
        }
    }

    override fun getUserProfileFlow(): Flow<Resource<User>> = flow {
        emit(Resource.Loading)

        try {
            val userDto = userApi.getUser()
            val user = userMapper.mapToDomain(userDto)

            userDao.insertUser(userMapper.mapToEntity(userDto))

            emit(Resource.Success(user))
        } catch (e: HttpException) {
            if (e.code() == 401 || e.code() == 403) {
                emit(Resource.Unauthorized)
            } else {
                // TODO: implement getting user id from cache
                val cached = null
                if (cached != null) {
                    emit(Resource.Success(cached))
                    emit(Resource.Error("Ошибка сервера: ${e.code()}"))
                } else {
                    emit(Resource.Error("Ошибка сервера: ${e.code()}"))
                }
            }
        } catch (e: IOException) {
            val cached = null
            if (cached != null) {
                emit(Resource.Success(cached))
                emit(Resource.Error("Нет сети. Показаны сохранённые данные"))
            } else {
                emit(Resource.Error("Нет соединения с интернетом"))
            }
        } catch (e: Exception) {
            val cached = null
            if (cached != null) {
                emit(Resource.Success(cached))
                emit(Resource.Error("Неизвестная ошибка"))
            } else {
                emit(Resource.Error("Неизвестная ошибка"))
            }
        }
    }

    override fun getAllUsersFlow(): Flow<List<User>> =
        userDao.getAllUsers().map { list -> list.map(userMapper::mapToDomain) }

    override suspend fun getCurrentUserId(): String? {
        return try {
            getCurrentUserProfile()?.id
        } catch (e: Exception) {
            null
        }
    }
}