package com.c22027.nyoombang.ui.profile.community

import androidx.lifecycle.ViewModel
import com.c22027.nyoombang.data.local.User

class CommunityProfileViewModel : ViewModel() {
    fun getCommunity() = User.community
}