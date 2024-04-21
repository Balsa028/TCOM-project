package com.example.tcomproject.api

import com.example.tcomproject.models.UserInfo
import com.example.tcomproject.models.Vehicle
import com.example.tcomproject.models.VehicleInfo
import com.example.tcomproject.response.UserInfoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitEndpoints {

    @POST("login")
    fun loginUser(@Body userInfo: UserInfo): Call<UserInfoResponse>

    @GET("allVehicles")
    fun getFreshStateOfAllVehicles(): Call<List<Vehicle>>

    @POST("addToFavorites")
    fun addToFavorites(@Body vehicleInfo: VehicleInfo): Call<Unit>

    @GET("vehicle/")
    fun getVehicleById(@Query("vehicleID") vehicleId: Int): Call<Vehicle>

}