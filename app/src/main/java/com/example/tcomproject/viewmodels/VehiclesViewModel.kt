package com.example.tcomproject.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tcomproject.models.Vehicle
import com.example.tcomproject.repositories.DataRepository

class VehiclesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DataRepository = DataRepository.getInstance(application)
    private var allVehicleList: List<Vehicle> = emptyList()
    private var selectedVehicleList: List<Vehicle> = emptyList()

    private val _selectedVehicleListLiveData = MutableLiveData<List<Vehicle>>()
    val selectedVehicleListLiveData: LiveData<List<Vehicle>> = _selectedVehicleListLiveData

    private val _allVehicleListLiveData = MutableLiveData<List<Vehicle>>()
    val allVehicleListLiveData: LiveData<List<Vehicle>> = _allVehicleListLiveData


    fun getAllVehiclesList() = repository.getAllVehiclesList()

    fun addToFavorites(vehicleId: Int) = repository.addToFavorites(vehicleId)

    fun getVehicleDetails(vehicleId: Int) = repository.getVehicleDetails(vehicleId)

    fun getFreshStateOfVehiclesList(): LiveData<List<Vehicle>?> = repository.vehicleList

    fun isLoading(): LiveData<Boolean> = repository.isLoading

    fun successfullyAddedToFavorites(): LiveData<Boolean> = repository.successfullyAddedToFavorites

    fun getVehicleById() = repository.vehicle

    fun updateSelectedVehicleList(list: List<Vehicle>) {
        _selectedVehicleListLiveData.value = list
    }

    fun updateAllVehicleList(list: List<Vehicle>) {
        _allVehicleListLiveData.value = list
    }

    fun getAllVehicleList(): List<Vehicle> = _allVehicleListLiveData.value ?: emptyList()

    fun getSelectedVehicleList(): List<Vehicle> = _selectedVehicleListLiveData.value ?: emptyList()

    fun getFilteredListBySelectedType(isAscending: Boolean, selectedVehicleType: Int): List<Vehicle> {
        allVehicleList = _allVehicleListLiveData.value ?: emptyList()
        val returnList = allVehicleList.filter { it.vehicleTypeID == selectedVehicleType }
        return if (isAscending) returnList.sortedBy { it.price } else returnList.sortedByDescending { it.price }
    }

    fun sortListAndReturn(isAscending: Boolean): List<Vehicle> {
        selectedVehicleList = _selectedVehicleListLiveData.value ?: emptyList()
        return if (isAscending) selectedVehicleList.sortedBy { it.price }
        else selectedVehicleList.sortedByDescending { it.price }
    }

    fun filterListBySearch(searchText: CharSequence): List<Vehicle> {
        selectedVehicleList = _selectedVehicleListLiveData.value ?: emptyList()
        return selectedVehicleList.filter { it.name.contains(searchText, ignoreCase = true) }
    }
}