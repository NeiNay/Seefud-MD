package com.seefud.seefud.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("status") val status: String? = null,
    @field:SerializedName("message") val message: String? = null
)

data class LoginResponse(
    @field:SerializedName("loginResult") val data: LoginResult? = null,
    @field:SerializedName("status") val status: String? = null,
    @field:SerializedName("message") val message: String? = null
)

data class LoginResult(
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("userId") val userId: String? = null,
    @field:SerializedName("token") val token: String? = null
)

data class VendorResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<VendorData>
)

data class VendorData(
    @SerializedName("id") val id: Int,
    @SerializedName("store_name") val store_name: String,
    @SerializedName("description") val description: String,
    @SerializedName("location") val location: String
)