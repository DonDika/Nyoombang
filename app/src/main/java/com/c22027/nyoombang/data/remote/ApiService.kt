package com.c22027.nyoombang.data.remote

import com.c22027.nyoombang.data.model.TransactionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("v2/{orderId}/status")
    fun getTransaction(
        @Path("orderId") orderId: String,
    ): Call<TransactionResponse>

}