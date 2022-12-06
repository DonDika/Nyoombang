package com.c22027.nyoombang.data.remote

import com.c22027.nyoombang.utils.Utilization
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    fun getApiService(): ApiService {
        val client = OkHttpClient.Builder()
            .apply {
                readTimeout(20, TimeUnit.SECONDS)
                writeTimeout(20, TimeUnit.SECONDS)
                connectTimeout(20, TimeUnit.SECONDS)
            }
            .addInterceptor(BasicAuthInterceptor(Utilization.TOKEN,""))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.sandbox.midtrans.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

}

