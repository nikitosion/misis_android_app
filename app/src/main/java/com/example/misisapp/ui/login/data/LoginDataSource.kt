package com.example.misisapp.ui.login.data

import android.content.Context
import android.util.Log
import com.example.misisapp.BuildConfig
import com.example.misisapp.ui.login.data.model.LoggedInUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

const val API_URL = BuildConfig.API_URL

class LoginDataSource (private val context: Context) {

    private val client = OkHttpClient()

    suspend fun loadUserFromToken(): Result<LoggedInUser> = withContext(Dispatchers.IO) {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val accessToken = prefs.getString("access_token", null)
        val tokenType = prefs.getString("token_type", null)

        if (accessToken == null || tokenType == null) {
            return@withContext Result.Error(IOException("Token not found"))
        }

        val urlUserInfo = "$API_URL/auth/me"

        val requestUserInfo = Request.Builder()
            .url(urlUserInfo)
            .get()
            .addHeader("Authorization", "$tokenType $accessToken")
            .build()

        try {
            val userInfoResponse = client.newCall(requestUserInfo).execute()
            userInfoResponse.use {
                if (it.isSuccessful) {
                    val userInfoJson = JSONObject(it.body?.string() ?: "{}")
                    val userID = userInfoJson.getString("id")
                    val username = userInfoJson.getString("username")
                    val fullName = userInfoJson.getString("full_name")
                    val email = userInfoJson.getString("email")
                    val role = userInfoJson.getString("role")
                    val groupID = userInfoJson.getString("group_id")
                    val isActive = userInfoJson.getString("is_active")

                    val urlGroupInfo = "$API_URL/groups/$groupID"
                    val requestGroupInfo = Request.Builder()
                        .url(urlGroupInfo)
                        .get()
                        .addHeader("Authorization", "$tokenType $accessToken")
                        .build()

                    // TODO: Catch situation when group (or smth else) null or body doesn't exist
                    /*val groupInfoResponse = client.newCall(requestGroupInfo).execute()
                    val groupInfoJson = JSONObject(groupInfoResponse.body?.string() ?: "{}")
                    val groupName = groupInfoJson.getString("name")*/

                    val groupName: String? = null


                    val user = LoggedInUser(userID, fullName, email, role, groupName, isActive.toBoolean(), username)
                    Result.Success(user)
                } else {
                    Result.Error(IOException("Ошибка загрузки профиля: ${it.code}"))
                }
            }
        } catch (e: Exception) {
            Result.Error(IOException("Ошибка запроса", e))
        }
    }

    suspend fun fetchUserInfo(tokenType: String, accessToken: String): Result<LoggedInUser> = withContext(Dispatchers.IO) {
        val url = "$API_URL/auth/me"

        Log.d("LoginFragment", "$tokenType $accessToken")

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("Authorization", "$tokenType $accessToken")
            .build()

        try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonResponse = JSONObject(responseBody ?: "{}")

                val userID = jsonResponse.getString("id")
                val displayName = jsonResponse.getString("username")

                val user = LoggedInUser(userID, displayName = displayName)
                Result.Success(user)
            } else {
                Result.Error(IOException("Failed to fetch user info: ${response.code}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error fetching user info", e))
        }
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> = withContext(
        Dispatchers.IO) {

        val urlToken = "$API_URL/auth/token"

        val formBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()

        Log.d("LoginData", urlToken)

        val tokenRequest = Request.Builder().url(urlToken).post(formBody).build()

        try {
            val response = client.newCall(tokenRequest).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonResponse = JSONObject(responseBody ?: "{}")

                val accessToken = jsonResponse.getString("access_token")
                val tokenType = jsonResponse.getString("token_type")

                val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                prefs.edit()
                    .putString("access_token", accessToken)
                    .putString("token_type", tokenType)
                    .apply()

                fetchUserInfo(tokenType, accessToken)
            } else {
                Result.Error(IOException("Login failed: ${response.code}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        context.deleteSharedPreferences("auth_prefs")
    }
}