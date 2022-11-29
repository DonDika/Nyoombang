package com.c22027.nyoombang.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.c22027.nyoombang.data.local.UserDataClass
import com.c22027.nyoombang.data.local.UserResponse
import com.c22027.nyoombang.repository.AppsRepositoryImpl


class LoginViewModel(private val repository: AppsRepositoryImpl = AppsRepositoryImpl()) :ViewModel() {


    private val _userLiveData = MutableLiveData<List<UserDataClass>>()
    val userLiveData: LiveData<List<UserDataClass>> = _userLiveData


    fun loginUsingLiveData(email: String,password: String) : LiveData<UserResponse>{
        return repository.login(email, password)
    }
}