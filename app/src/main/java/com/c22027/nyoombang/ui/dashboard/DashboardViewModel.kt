package com.c22027.nyoombang.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.c22027.nyoombang.data.model.EventResponse
import com.c22027.nyoombang.data.model.UserResponse
import com.c22027.nyoombang.repository.AppsRepositoryImpl

class DashboardViewModel(private val repository: AppsRepositoryImpl = AppsRepositoryImpl()):ViewModel() {


    fun getEventUser(): LiveData<EventResponse> = repository.getEventUser()

    fun getEventCommunity(userId: String): LiveData<EventResponse> = repository.getEventCommunity(userId)
}