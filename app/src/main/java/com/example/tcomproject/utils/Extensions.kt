package com.example.tcomproject.utils

    fun String.addEuroCurrency(): String {
        return "$this €"
    }

    fun Double.formatDoubleForDisplay(): String {
        return String.format("%.6f", this)
    }