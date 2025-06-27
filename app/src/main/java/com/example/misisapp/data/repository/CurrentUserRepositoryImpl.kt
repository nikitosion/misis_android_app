package com.example.misisapp.data.repository

import com.example.misisapp.data.local.CurrentUserPrefs
import com.example.misisapp.data.mapper.UserMapper
import com.example.misisapp.data.remote.api.UserApi
import com.example.misisapp.domain.model.User
import com.example.misisapp.domain.repository.CurrentUserRepository
import com.example.misisapp.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentUserRepositoryImpl @Inject constructor(
    private val prefs: CurrentUserPrefs,
    private val userApi: UserApi,
    private val userMapper: UserMapper,
    private val json: Json
) : CurrentUserRepository {

    /**
     * Getting new user's data from server, using userApi, and saving it locally.
     * @see userApi
     * */
    override suspend fun getUpdateAndSaveProfile() : Resource<User> {
        return try {
            val userDto = userApi.getUser()
            val user = userMapper.mapToDomain(userDto)
            saveProfile(user)

            Resource.Success(user)
        } catch (e: HttpException) {
            if (e.code() == 401) Resource.Unauthorized
            else Resource.Error("Ошибка сервера: ${e.code()}")
        } catch (e: IOException) {
            Resource.Error("Нет соединения с интернетом")
        } catch (e: Exception) {
            Resource.Error("Неизвестная ошибка")
        }
    }


    override fun tokenFlow(): Flow<String?> = prefs.getTokenFlow()

    override suspend fun saveToken(token: String) = prefs.saveToken(token)

    override suspend fun clearToken() = prefs.clearToken()

    override fun profileCacheFlow(): Flow<User?> =
        prefs.getProfileFlow().map { jsonString ->
            jsonString?.let { json.decodeFromString<User>(it) }
        }

    override suspend fun saveProfile(user: User) {
        prefs.saveProfileJson(json.encodeToString(user))
    }

    override suspend fun clearCachedProfile() {
        prefs.clearProfileJson()
    }

}