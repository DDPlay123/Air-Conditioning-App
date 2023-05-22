package com.side.project.air.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    /**
     * HTTP 攔截器
     */
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(LogInterceptor())
        .build()

    /**
     * 中央氣象 API
     */
    val weatherServer: ApiService by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://opendata.cwb.gov.tw/api/v1/rest/datastore/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}