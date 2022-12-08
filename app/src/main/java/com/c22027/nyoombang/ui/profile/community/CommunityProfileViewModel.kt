package com.c22027.nyoombang.ui.profile.community

import androidx.lifecycle.ViewModel
import com.c22027.nyoombang.repository.AppsRepositoryImpl


class CommunityProfileViewModel(private val repository: AppsRepositoryImpl = AppsRepositoryImpl()) : ViewModel() {
    fun getCommunity(id: String) = repository.getUser(id)
}