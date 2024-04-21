package com.example.tcomproject.adapters

interface AdapterListener {
    fun onToVehicleDetailsScreen(vehicleId: Int)
    fun onAddToFavorites(vehicleId: Int)
}