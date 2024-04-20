package com.example.tcomproject.utils

import android.app.ProgressDialog
import android.content.Context

object Util {

    fun saveTokenInSharedPrefs(token: String, context: Context) {
        val preferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        preferences.edit().apply {
            putString(AUTH_TOKEN, token)
        }.apply()
    }

    fun getTokenFromSharedPrefs(context: Context) : String? {
        val preferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        return preferences.getString(AUTH_TOKEN, "")
    }


}