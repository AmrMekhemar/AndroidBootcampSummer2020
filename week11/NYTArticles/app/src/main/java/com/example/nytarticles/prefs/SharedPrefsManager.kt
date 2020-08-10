package com.example.nytarticles.prefs

import android.content.Context
import splitties.init.appCtx

class SharedPrefsManager {

    private val KEY_LOGGED_IN = ""
    private val MOVIES_SHARED_PREFS = ""
    private val context = appCtx
    private val prefs = context.getSharedPreferences(MOVIES_SHARED_PREFS, Context.MODE_PRIVATE)

    fun setUserLoggedIn(isLoggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_LOGGED_IN, isLoggedIn).apply()
    }

    fun isUserLoggedIn() = prefs.getBoolean(KEY_LOGGED_IN, false)
}
