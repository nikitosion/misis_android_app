package com.example.misisapp.data.repository

import com.example.misisapp.data.datastore.TokenDataStore
import com.example.misisapp.data.remote.api.AuthApi
import com.example.misisapp.data.remote.api.UserApi
import com.example.misisapp.data.session.SessionManager
import com.example.misisapp.domain.model.auth.AuthState
import com.example.misisapp.domain.repository.AuthRepository
import com.example.misisapp.domain.repository.CurrentUserRepository
import com.example.misisapp.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val currentUserRepository: CurrentUserRepository,
    private val tokenDataStore: TokenDataStore,
    private val sessionManager: SessionManager,
): AuthRepository {

    override suspend fun login(username: String, password: String): Resource<Unit> {
        return try {
            // Getting token from server and saving it
            val response = authApi.login(username, password)
            currentUserRepository.saveToken(response.accessToken)

            // Getting current user's data and saving it locally
            currentUserRepository.getUpdateAndSaveProfile()

            Resource.Success(Unit)
        } catch (e: HttpException) {
            if (e.code() == 401) Resource.Unauthorized
            else Resource.Error("Ошибка сервера: ${e.code()}")
        } catch (e: IOException) {
            Resource.Error("Нет соединения с интернетом")
        } catch (e: Exception) {
            Resource.Error("Неизвестная ошибка")
        }
    }

    override suspend fun recoverPassword(email: String): Boolean {
        /*return try {
            val response = authApi.recoverPassword(RecoverPasswordRequestDto(email))
            response.isSuccessful
        } catch (e: Exception) {
            false
        }*/
        return false
    }

    override suspend fun logout() {
        try {
            currentUserRepository.clearToken()
            currentUserRepository.clearCachedProfile()
            sessionManager.notifyUnauthorized()
        } catch (e: Exception){
        }
    }

    override suspend fun getAuthToken(): String? = tokenDataStore.getToken()

    override suspend fun saveAuthToken(token: String) = tokenDataStore.saveToken(token)

    override suspend fun clearAuthToken() = tokenDataStore.clearToken()

    override fun getAuthState(): Flow<AuthState> {
        return tokenDataStore.hasValidToken().map { hasToken ->
            if (hasToken) {
                AuthState.Authenticated
            } else {
                AuthState.NotAuthenticated
            }
        }
    }

    override suspend fun validateToken(): Boolean {
        return try {
            val token = getAuthToken()
            if (token.isNullOrEmpty()) return false
            userApi.getUser()
            true
        } catch (e: HttpException) {
            when(e.code()) {
                401, 403 -> {
                    clearAuthToken()
                    false
                }
                else -> {
                    true
                }
            }
        } catch (e: Exception) {
            false
        }
    }
}