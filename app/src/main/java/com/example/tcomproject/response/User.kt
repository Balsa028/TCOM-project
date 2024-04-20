package com.example.tcomproject.response

import com.google.gson.annotations.Expose

data class User (
    @Expose
    val id: Int,
    @Expose
    val email: String
) {
}