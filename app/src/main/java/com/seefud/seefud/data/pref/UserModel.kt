package com.seefud.seefud.data.pref

data class UserModel(
    val name: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)

data class VendorModel(
    val id: Int,
    val store_name: String,
    val description: String,
    val location: String,
    val rating: Int = 0,
    val is_verified: Boolean = false
)