package com.example.tcomproject.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tcomproject.fragments.VehicleDetailsFragment
import java.lang.IllegalArgumentException

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(application = application) as T
            modelClass.isAssignableFrom(VehiclesViewModel::class.java) -> VehiclesViewModel(application = application) as T
            modelClass.isAssignableFrom(VehicleDetailsViewModel::class.java) -> VehicleDetailsViewModel(application = application) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}