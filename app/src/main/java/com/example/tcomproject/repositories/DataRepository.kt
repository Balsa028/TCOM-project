package com.example.tcomproject.repositories

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tcomproject.api.RetrofitEndpoints
import com.example.tcomproject.api.RetrofitInstance
import com.example.tcomproject.models.UserInfo
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


    companion object {
        private var INSTANCE: DataRepository? = null

        fun getInstance(application: Application) : DataRepository = INSTANCE ?: run {
            INSTANCE = DataRepository(application = application)
            INSTANCE!!
        }
    }
}