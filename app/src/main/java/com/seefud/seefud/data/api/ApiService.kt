package com.seefud.seefud.data.api

import com.seefud.seefud.data.response.AllVendorsResult
import com.seefud.seefud.data.response.LoginRequest
import com.seefud.seefud.data.response.LoginResponse
import com.seefud.seefud.data.response.ProductData
import com.seefud.seefud.data.response.ProductResponse
import com.seefud.seefud.data.response.RegisterRequest
import com.seefud.seefud.data.response.RegisterResponse
import com.seefud.seefud.data.response.VendorData
import com.seefud.seefud.data.response.VendorDetailResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("auth/register")
    suspend fun register(
        @Body requestBody: RegisterRequest
    ): RegisterResponse

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @GET("/vendor")
    suspend fun getAllVendors(): AllVendorsResult

    @Multipart
    @POST("/vendor")
    suspend fun createVendor(
        @Header("Authorization") authHeader: String,
        @Part("store_name") storeName: RequestBody,
        @Part("description") description: RequestBody,
        @Part("location") location: RequestBody,
        @Part image: MultipartBody.Part?
    ): VendorData

    @Multipart
    @PUT("/vendor/{id}")
    suspend fun updateVendor(
        @Path("id") vendorId: Int,
        @Part("store_name") storeName: RequestBody,
        @Part("description") description: RequestBody,
        @Part("location") location: RequestBody,
        @Part image: MultipartBody.Part?
    ): VendorData

    @GET("/vendor/{vendorId}")
    suspend fun getVendorById(
        @Path("vendorId") vendorId: Int
    ): VendorDetailResult

    @GET("/products/{vendorId}")
    suspend fun getVendorsProduct(
        @Path("vendorId") vendorId: Int
    ): ProductResponse

    @Multipart
    @POST("/products")
    suspend fun createProduct(
        @Header("Authorization") authHeader: String, @Part body: MultipartBody
    ): ProductData
}