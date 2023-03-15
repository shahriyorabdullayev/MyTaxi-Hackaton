package me.shakhriyor.mytaxihackaton.common

import android.content.Context

class SharedPref(private val context: Context) {

    private val pref = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

    fun saveBoolean(key: String, value: Boolean) {
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return pref.getBoolean(key, false)
    }


}