package com.raysha.news.models

data class LoginResponse (
    val success: Boolean,
    val message: String,
    val username: String?
)
