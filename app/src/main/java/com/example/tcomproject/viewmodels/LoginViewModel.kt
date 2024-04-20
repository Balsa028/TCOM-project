package com.example.tcomproject.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.tcomproject.repositories.DataRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DataRepository = DataRepository.getInstance(application)

    fun initiateLogin(email: String) = repository.initiateLogin(email, getApplication<Application>().applicationContext)

    fun isLoading(): LiveData<Boolean> = repository.isLoading

    fun isLoginSuccess(): LiveData<Boolean> = repository.loginSuccess
}