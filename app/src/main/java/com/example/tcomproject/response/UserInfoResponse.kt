package com.example.tcomproject.response

import com.google.gson.annotations.Expose

data class UserInfoResponse (
    @Expose
    val user: User,
    @Expose
    val token: String
) {
}