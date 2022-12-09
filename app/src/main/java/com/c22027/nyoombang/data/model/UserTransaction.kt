package com.c22027.nyoombang.data.model


data class UserTransaction(
    var id: String?,
    val userId: String,
    val orderId: String,
    val username: String,
    val eventName: String,
    val eventId: String,
    val amount: String,
    val status: String,
    val transactionTime: String,
    val transactionDate: String
)
