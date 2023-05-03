package com.uc.machiapp.token

import android.content.Context
import android.content.SharedPreferences

class MyPreferences(context: Context) {
    private val mContext: Context = context
    private val sharedPref: SharedPreferences = mContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    //Fungsi Buat Kelola Data User
    fun setUsername(username: String) {
        val editor = sharedPref.edit()
        editor.putString("username", username)
        editor.apply()
    }

    fun getUsername(): String? {
        return sharedPref.getString("username","")
    }

    //Fungsi Buat Atur Session
    fun setIsLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean("is_logged_in", isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPref.getBoolean("is_logged_in", false)
    }

    //Fungsi Buat Kelola Token
    fun setToken(token: String) {
        val editor = sharedPref.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPref.getString("token", "")
    }
}