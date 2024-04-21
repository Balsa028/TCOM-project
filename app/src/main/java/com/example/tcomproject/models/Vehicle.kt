package com.example.tcomproject.models

import com.google.gson.annotations.Expose

data class Vehicle(
    @Expose
    val vehicleID: Int,
    @Expose
    val vehicleTypeID: Int,
    @Expose
    val imageURL: String,
    @Expose
    val name: String,
    @Expose
    val location: Location,
    @Expose
    val rating: Float,
    @Expose
    val price: Int,
    @Expose
    val isFavorite: Boolean
    )