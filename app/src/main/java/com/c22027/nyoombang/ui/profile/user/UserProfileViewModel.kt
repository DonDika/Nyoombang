package com.c22027.nyoombang.ui.profile.user

import androidx.lifecycle.ViewModel
import com.c22027.nyoombang.data.local.Donation
import com.c22027.nyoombang.data.local.User

class UserProfileViewModel : ViewModel() {
    fun getUser() = User.normalUser

    fun getDonations() = Donation.provideDonations()
}