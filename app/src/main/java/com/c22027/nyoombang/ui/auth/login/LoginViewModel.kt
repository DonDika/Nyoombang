package com.c22027.nyoombang.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.c22027.nyoombang.data.model.UserResponse
import com.c22027.nyoombang.repository.AppsRepositoryImpl


class LoginViewModel(private val repository: AppsRepositoryImpl = AppsRepositoryImpl()) :ViewModel() {



    fun loginUsingLiveData(email: String,password: String) : LiveData<UserResponse>{
        return repository.login(email, password)
    }
}