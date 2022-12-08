package com.c22027.nyoombang.ui.profile.user

import androidx.lifecycle.ViewModel
import com.c22027.nyoombang.repository.AppsRepositoryImpl

//import com.c22027.nyoombang.data.local.Donation
//import com.c22027.nyoombang.data.local.User


class UserProfileViewModel(private val repository: AppsRepositoryImpl = AppsRepositoryImpl()) : ViewModel() {
    fun getUser(id: String) = repository.getUser(id)

//    fun getDonations() = Donation.provideDonations()
}