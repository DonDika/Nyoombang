package com.c22027.nyoombang.data.model

data class UserTransaction(
    var id: String?,
    val userId: String,
    val orderId: String,
    val username: String,
    val campaignName: String,
    val amount: String,
    val status: String,
    val transactionTime: String,
)
