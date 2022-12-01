package com.c22027.nyoombang.data.local

import android.content.Context
import android.content.SharedPreferences


class PreferenceManager(context: Context){

    //instance
    private val PREF_NAME = "nyoombang.pref"
    private var sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    //helper
    /*
    put for save
    */
    fun put(key: String, value: String){
        editor.putString(key, value).apply()
    }

    fun put(key: String, value: Int){
        editor.putInt(key, value).apply()
    }

    /*
    get for get data
    */
    fun getString(key: String): String?{
        return sharedPreferences.getString(key, "")
    }

    fun getInt(key: String): Int?{
        return sharedPreferences.getInt(key, 0)
    }

    /*
    delete data
    */
    fun clear(){
        editor.putInt("pref_is_login", 0).apply()
        editor.remove("order_id").apply()
    }

    fun clearOrderId(){
        editor.remove("order_id").apply()
    }

}




