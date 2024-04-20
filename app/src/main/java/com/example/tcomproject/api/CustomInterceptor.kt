package com.example.tcomproject.api

import android.content.Context
import com.example.tcomproject.utils.EMPTY_STRING
import com.example.tcomproject.utils.Util
import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken = Util.getTokenFromSharedPrefs(context)

        val requestBuilder = chain.request().newBuilder()
            .addHeader("Content-Type","application/json")
            .addHeader("Accept","application/json")
            .addHeader("X-api-key","vekq8ne97uryr3mj4iudv8um07ggmhcat874q96jzvyypabgrm3zhyrwcgybm4hk")

        if (authToken != EMPTY_STRING)
            requestBuilder.addHeader("Authorization", "Bearer $authToken")

        return chain.proceed(requestBuilder.build())
    }
}