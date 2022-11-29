package com.c22027.nyoombang.data.local

data class UserResponse(
    var items: List<UserDataClass>? =null,
    var exception: Exception? = null
)
