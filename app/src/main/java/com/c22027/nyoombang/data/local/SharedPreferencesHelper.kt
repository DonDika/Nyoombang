package com.c22027.nyoombang.data.local

import android.content.*
import com.c22027.nyoombang.utils.Constant

class SharedPreferencesHelper(context: Context) {

    private val PREF_NAME = "sharedpref"
    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)


    var prefUid: String?
        get() = preferences.getString(Constant.PREF_UID, "")
        set(value) = preferences.edit().putString(Constant.PREF_UID, value).apply()

    var prefUsername: String?
        get() = preferences.getString(Constant.PREF_USERNAME, "")
        set(value) = preferences.edit().putString(Constant.PREF_USERNAME, value).apply()


    var prefStatus: Boolean
        get() = preferences.getBoolean(Constant.PREF_IS_LOGIN, false)
        set(value) = preferences.edit().putBoolean(Constant.PREF_IS_LOGIN, value).apply()

    var prefLevel: String?
        get() = preferences.getString(Constant.PREF_LEVEL, "")
        set(value) = preferences.edit().putString(Constant.PREF_LEVEL, value).apply()

    var prefOrderId: String?
        get() = preferences.getString(Constant.PREF_ORDERID, "")
        set(value) = preferences.edit().putString(Constant.PREF_ORDERID, value).apply()

    var prefNewAccess: Boolean
        get() =  preferences.getBoolean(Constant.PREF_NEW_ACCESS, true)
        set(value) = preferences.edit().putBoolean(Constant.PREF_NEW_ACCESS,value).apply()

    var prefAmount: Int
        get() =  preferences.getInt(Constant.PREF_AMOUNT, 0)
        set(value) = preferences.edit().putInt(Constant.PREF_AMOUNT, value).apply()


    var prefPhone: String?
        get() =  preferences.getString(Constant.PREF_PHONE, "")
        set(value) = preferences.edit().putString(Constant.PREF_PHONE, value).apply()

    var prefEmail: String?
        get() =  preferences.getString(Constant.PREF_EMAIL, "")
        set(value) = preferences.edit().putString(Constant.PREF_EMAIL, value).apply()



    var inputDonation: String?
        get() =  preferences.getString(Constant.INPUT_DONATION, "")
        set(value) = preferences.edit().putString(Constant.INPUT_DONATION, value).apply()





    fun prefClear(){
        preferences.edit().remove(Constant.PREF_IS_LOGIN).apply()
        preferences.edit().remove(Constant.PREF_LEVEL).apply()
        preferences.edit().remove(Constant.PREF_USERNAME).apply()
        preferences.edit().remove(Constant.PREF_PHONE).apply()
        preferences.edit().remove(Constant.PREF_UID).apply()
        preferences.edit().remove(Constant.PREF_EMAIL).apply()

    }


    fun clearOrderId(){
        preferences.edit().remove(Constant.PREF_ORDERID).apply()
    }

    fun clearAmount(){
        preferences.edit().remove(Constant.PREF_AMOUNT).apply()
    }

    fun inputDonation(){
        preferences.edit().remove(Constant.INPUT_DONATION).apply()
    }

}