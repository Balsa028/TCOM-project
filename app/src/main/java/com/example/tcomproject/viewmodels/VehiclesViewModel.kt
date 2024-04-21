package com.example.tcomproject.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tcomproject.models.Vehicle
import com.example.tcomproject.repositories.DataRepository

class VehiclesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DataRepository = DataRepository.getInstance(application)

    fun getAllVehiclesList() = repository.getAllVehiclesList()

    fun addToFavorites(vehicleId: Int) = repository.addToFavorites(vehicleId)

    fun getFreshStateOfVehiclesList(): LiveData<List<Vehicle>?> = repository.vehicleList

    fun isLoading(): LiveData<Boolean> = repository.isLoading

    fun successfullyAddedToFavorites(): LiveData<Boolean> = repository.successfullyAddedToFavorites

}