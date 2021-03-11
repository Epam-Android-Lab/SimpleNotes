package com.example.simplenotes.presentation.main

import android.content.Context
import android.content.SharedPreferences

class SaveTheme(val context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences("Theme", 0)
    fun setDarkModeState(state: Boolean?) {
        val editor = prefs.edit()
        state?.let { editor.putBoolean("Dark", it) }
        editor.apply()
    }

    fun loadDarkModeState(): Boolean {
        return prefs.getBoolean("Dark", false)
    }
}