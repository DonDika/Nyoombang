package com.c22027.nyoombang.data.model

data class UserTransaction(
    var orderId: String,
    val userId: String,
    val eventId: String,
    val name: String,
    val campaignName: String,
    val amount: String,
    val status: String,
    val transactionTime: String,
    val eventIdStatus : String,
    val userIdStatus: String
)
