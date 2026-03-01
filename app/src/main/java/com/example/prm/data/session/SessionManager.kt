package com.example.prm.data.session

import android.content.Context
import android.content.SharedPreferences
import com.example.prm.data.remote.dto.AuthResponse

class SessionManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(
        "badmini_session",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_EXPIRES_AT = "expires_at"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_EMAIL = "email"
        private const val KEY_NAME = "full_name"
        private const val KEY_PHONE = "phone"
        private const val KEY_AVATAR = "avatar"
    }

    fun saveAuthResponse(authResponse: AuthResponse) {
        sharedPref.edit().apply {
            putString(KEY_ACCESS_TOKEN, authResponse.accessToken)
            putString(KEY_REFRESH_TOKEN, authResponse.refreshToken)
            putString(KEY_EXPIRES_AT, authResponse.expiresAt)
            
            putString(KEY_USER_ID, authResponse.user.id)
            putString(KEY_EMAIL, authResponse.user.email)
            putString(KEY_NAME, authResponse.user.fullName)
            putString(KEY_PHONE, authResponse.user.phone)
            putString(KEY_AVATAR, authResponse.user.avatarUrl)
        }.apply()
    }

    fun saveToken(token: String) {
        sharedPref.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun getAccessToken(): String? = sharedPref.getString(KEY_ACCESS_TOKEN, null)
    fun getRefreshToken(): String? = sharedPref.getString(KEY_REFRESH_TOKEN, null)

    fun saveUserInfo(userId: String, email: String, name: String, phone: String? = null, avatar: String? = null) {
        sharedPref.edit().apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_EMAIL, email)
            putString(KEY_NAME, name)
            if (phone != null) putString(KEY_PHONE, phone)
            if (avatar != null) putString(KEY_AVATAR, avatar)
        }.apply()
    }

    fun getUserId(): String? = sharedPref.getString(KEY_USER_ID, null)
    fun getEmail(): String? = sharedPref.getString(KEY_EMAIL, null)
    fun getName(): String? = sharedPref.getString(KEY_NAME, null)
    fun getPhone(): String? = sharedPref.getString(KEY_PHONE, null)
    fun getAvatar(): String? = sharedPref.getString(KEY_AVATAR, null)

    fun logout() {
        sharedPref.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean = getAccessToken() != null
}
