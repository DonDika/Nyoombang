package com.c22027.nyoombang.ui.profile.user

import androidx.lifecycle.ViewModel
//import com.c22027.nyoombang.data.local.Donation
//import com.c22027.nyoombang.data.local.User
import com.c22027.nyoombang.data.remote.repository.AppsRepositoryImpl

class UserProfileViewModel(private val repository: AppsRepositoryImpl = AppsRepositoryImpl()) : ViewModel() {
    fun getUser(id: String) = repository.getUser(id)

//    fun getDonations() = Donation.provideDonations()
}