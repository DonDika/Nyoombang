package com.c22027.nyoombang.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.c22027.nyoombang.data.local.UserDataClass

class LoginViewModel :ViewModel() {

    private val _userLiveData = MutableLiveData<List<UserDataClass>>()
    val userLiveData : LiveData<List<UserDataClass>> = _userLiveData


}