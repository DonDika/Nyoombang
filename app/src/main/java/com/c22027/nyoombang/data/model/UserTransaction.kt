package com.c22027.nyoombang.data.model

data class UserTransaction(
    var orderId: String? =null,
    val userId: String? =null,
    val eventId: String? =null,
    val name: String? =null,
    val campaignName: String? =null,
    val amount: String? =null,
    val status: String? =null,
    val transactionTime: String? =null,
    val eventIdStatus : String? =null,
    val userIdStatus: String? =null
)
