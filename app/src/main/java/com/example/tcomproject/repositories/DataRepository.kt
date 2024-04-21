package com.example.tcomproject.repositories

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tcomproject.api.RetrofitEndpoints
import com.example.tcomproject.api.RetrofitInstance
import com.example.tcomproject.models.UserInfo
import com.example.tcomproject.models.Vehicle
import com.example.tcomproject.models.VehicleInfo
import com.example.tcomproject.response.UserInfoResponse
import com.example.tcomproject.utils.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataRepository private constructor(application: Application) {

    private val apiService = RetrofitInstance.getInstance(application.applicationContext).create(RetrofitEndpoints::class.java)

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

   private val _loginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    private val _successfullyAddedToFavorites: MutableLiveData<Boolean> = MutableLiveData()
    val successfullyAddedToFavorites: LiveData<Boolean>
        get() = _successfullyAddedToFavorites

    private val _vehicleList: MutableLiveData<List<Vehicle>?> = MutableLiveData()
    val vehicleList: LiveData<List<Vehicle>?>
        get() = _vehicleList

    private val _vehicle: MutableLiveData<Vehicle?> = MutableLiveData()
    val vehicle: LiveData<Vehicle?>
        get() = _vehicle


    fun initiateLogin(email: String, context: Context) {
        if (email.isNotEmpty()) {
            _isLoading.postValue(true)
            apiService.loginUser(UserInfo(email)).enqueue(object : Callback<UserInfoResponse> {
                override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                    if (response.isSuccessful && response.code() == 200 && response.body() != null) {
                        _isLoading.postValue(false)
                        val token = response.body()!!.token
                        Util.saveTokenInSharedPrefs(token, context)
                        _loginSuccess.postValue(true)
                    } else {
                        _isLoading.postValue(false)
                        _loginSuccess.postValue(false)
                    }

                }

                override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                    t.printStackTrace()
                }

            })
        }
    }

    fun getAllVehiclesList() {
        _isLoading.postValue(true)
        apiService.getFreshStateOfAllVehicles().enqueue(object : Callback<List<Vehicle>> {
            override fun onResponse(call: Call<List<Vehicle>>, response: Response<List<Vehicle>>) {
                if (response.isSuccessful && response.code() == 200 && response.body() != null) {
                    _isLoading.postValue(false)
                    val listOfAllVehicles = response.body()
                    _vehicleList.postValue(listOfAllVehicles)
                } else {
                    _isLoading.postValue(false)
                    _vehicleList.postValue(null)
                }
            }

            override fun onFailure(call: Call<List<Vehicle>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun addToFavorites(vehicleId: Int) {
        apiService.addToFavorites(VehicleInfo(vehicleId)).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful && response.code() == 200)
                    _successfullyAddedToFavorites.postValue(true)
                else
                    _successfullyAddedToFavorites.postValue(false)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getVehicleDetails(vehicleId: Int) {
        _isLoading.postValue(true)
        apiService.getVehicleById(vehicleId).enqueue(object : Callback<Vehicle> {
            override fun onResponse(call: Call<Vehicle>, response: Response<Vehicle>) {
                if (response.isSuccessful && response.code() == 200 && response.body() != null) {
                    _isLoading.postValue(false)
                    _vehicle.postValue(response.body())
                } else {
                    _isLoading.postValue(false)
                    _vehicle.postValue(null)
                }
            }

            override fun onFailure(call: Call<Vehicle>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


    companion object {
        private var INSTANCE: DataRepository? = null

        fun getInstance(application: Application) : DataRepository = INSTANCE ?: run {
            INSTANCE = DataRepository(application = application)
            INSTANCE!!
        }
    }
}