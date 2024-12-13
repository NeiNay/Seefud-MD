package com.seefud.seefud.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("status") val status: String? = null,
    @field:SerializedName("message") val message: String? = null,
    @field:SerializedName("data") val data: RegisterData? = null
)

data class RegisterData(
    @field:SerializedName("user") val user: User? = null,
    @field:SerializedName("token") val token: String? = null
)

data class User(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("email") val email: String,
    @field:SerializedName("role") val role: String
)

data class RegisterRequest(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("email") val email: String,
    @field:SerializedName("password") val password: String,
    @field:SerializedName("role") val role: String = "vendor"
)

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @field:SerializedName("data") val data: LoginResult? = null,
    @field:SerializedName("status") val status: String? = null,
    @field:SerializedName("message") val message: String? = null
)

data class LoginResult(
    @field:SerializedName("id") val id: Int? = null,
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("email") val email: String? = null,
    @field:SerializedName("role") val role: String? = "vendor",
    @field:SerializedName("token") val token: String? = null
)

data class AllVendorsResult(
    @field:SerializedName("status") val status: String? = null,
    @field:SerializedName("message") val message: String? = null,
    @field:SerializedName("data") val listVendor: List<VendorData> = emptyList()
)

data class VendorDetailResult(
    @field:SerializedName("status") val status: String? = null,
    @field:SerializedName("message") val message: String? = null,
    @field:SerializedName("data") val detailVendor: VendorData
)

data class VendorData(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("store_name") val storename: String? = null,
    @field:SerializedName("description") val description: String? = null,
    @field:SerializedName("location") val location: String? = null,
    @field:SerializedName("rating") val rating: Int = 0,
    @field:SerializedName("is_verified") val isVerified: Boolean = false
)

data class ProductResponse(
    @field:SerializedName("status") val status: String? = null,
    @field:SerializedName("message") val message: String? = null,
    @field:SerializedName("data") val listProduct: List<ProductData> = emptyList()
)

data class ProductData(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("description") val description: String? = null,
    @field:SerializedName("price") val price: Int,
    @field:SerializedName("qr_code") val qrCode: String? = null
)



