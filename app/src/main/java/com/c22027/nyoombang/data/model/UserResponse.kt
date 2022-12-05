package com.c22027.nyoombang.data.model

data class UserResponse(
    var items: List<UserDataClass>? =null,
    var exception: Exception? = null
)
