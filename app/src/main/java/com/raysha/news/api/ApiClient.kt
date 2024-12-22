package com.raysha.news.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient{
    private const val BASE_URL = "http://192.168.220.149/"

    private fun interceptor(): OkHttpClient{
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }


    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(interceptor()) // Menggunakan client yang telah dibangun
            .addConverterFactory(GsonConverterFactory.create()) // Menambahkan converter Gson
            .build()
            .create(ApiService::class.java) // Membuat ApiService
    }
}