package com.example.prm.data.session

import android.content.Context

class SessionManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    fun getToken(): String? =
        prefs.getString("access_token", null)

    fun clear() {
        prefs.edit().clear().apply()
    }
}

