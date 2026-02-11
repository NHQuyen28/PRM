package com.example.prm.data.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(
        "badmini_session",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_EMAIL = "email"
        private const val KEY_NAME = "name"
    }

    fun saveToken(token: String) {
        sharedPref.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? = sharedPref.getString(KEY_TOKEN, null)

    fun saveUserInfo(userId: Int, email: String, name: String) {
        sharedPref.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_EMAIL, email)
            putString(KEY_NAME, name)
        }.apply()
    }

    fun getUserId(): Int = sharedPref.getInt(KEY_USER_ID, -1)
    fun getEmail(): String? = sharedPref.getString(KEY_EMAIL, null)
    fun getName(): String? = sharedPref.getString(KEY_NAME, null)

    fun logout() {
        sharedPref.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean = getToken() != null
}
