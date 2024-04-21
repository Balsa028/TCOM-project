package com.example.tcomproject.models

import com.google.gson.annotations.Expose

data class Location(
    @Expose
    val latitude: Double,
    @Expose
    val longitude: Double
)
