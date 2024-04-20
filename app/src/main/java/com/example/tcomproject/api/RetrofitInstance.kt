package com.example.tcomproject.api

import android.content.Context
import com.example.tcomproject.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private var INSTANCE: Retrofit? = null

    fun getInstance(context: Context): Retrofit = INSTANCE ?: run {
        val loggingInterceptor = CustomInterceptor(context)
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}