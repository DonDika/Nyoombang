package com.c22027.nyoombang.helper

import android.content.*

class SharedPreferencesHelper(context: Context) {

    private val PREF_NAME = "sharedpref"



    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
    var prefUid : String?
    get() = preferences.getString(Constant.PREF_UID,"")
    set(value) = preferences.edit().putString(Constant.PREF_UID,value).apply()

    var prefStatus: Boolean
    get() = preferences.getBoolean(Constant.PREF_IS_LOGIN,false)
    set(value) = preferences.edit().putBoolean(Constant.PREF_IS_LOGIN,value).apply()

    var prefLevel: String?
    get() = preferences.getString(Constant.PREF_LEVEL,"")
    set(value) = preferences.edit().putString(Constant.PREF_LEVEL,value).apply()

    fun prefClear(){
        preferences.edit().remove(Constant.PREF_IS_LOGIN).apply()
        preferences.edit().remove(Constant.PREF_LEVEL).apply()

    }


}