package com.example.tcomproject.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.tcomproject.models.Vehicle
import com.example.tcomproject.repositories.DataRepository

class VehicleDetailsViewModel(application: Application) : AndroidViewModel(application) {

    val repository: DataRepository = DataRepository.getInstance(application)

    fun getVehicleDetails(vehicleId: Int) = repository.getVehicleDetails(vehicleId)

    fun addToFavorites(vehicleId: Int) = repository.addToFavorites(vehicleId)

    fun getAllVehiclesList() = repository.getAllVehiclesList()

    fun getFreshStateOfVehiclesList(): LiveData<List<Vehicle>?> = repository.vehicleList

    fun successfullyAddedToFavorites(): LiveData<Boolean> = repository.successfullyAddedToFavorites

    fun getVehicleById() = repository.vehicle

    fun isLoading() = repository.isLoading



}