package com.example.tcomproject.api

import com.example.tcomproject.models.UserInfo
import com.example.tcomproject.response.UserInfoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitEndpoints {

    @POST("login")
    fun loginUser(@Body userInfo: UserInfo) : Call<UserInfoResponse>

}