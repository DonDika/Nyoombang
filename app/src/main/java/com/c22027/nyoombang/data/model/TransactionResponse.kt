package com.c22027.nyoombang.data.model

import com.google.gson.annotations.SerializedName

data class TransactionResponse(

	@field:SerializedName("order_id")
	val orderId: String,

	@field:SerializedName("gross_amount")
	val grossAmount: String,

	@field:SerializedName("transaction_status")
	val transactionStatus: String,

	@field:SerializedName("transaction_time")
	val transactionTime: String,


)