package com.c22027.nyoombang.data.model


data class UserDataClass (
    val user_id: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val role: String,
    val name: String,
    val description: String? = null,
    val twitter: String? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val picture: String? = null
)




