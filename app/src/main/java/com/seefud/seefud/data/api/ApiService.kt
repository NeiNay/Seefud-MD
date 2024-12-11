package com.seefud.seefud.data.api

import com.seefud.seefud.data.response.AllVendorsResult
import com.seefud.seefud.data.response.LoginResponse
import com.seefud.seefud.data.response.ProductData
import com.seefud.seefud.data.response.ProductResponse
import com.seefud.seefud.data.response.RegisterResponse
import com.seefud.seefud.data.response.VendorDetailResult
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String = "vendor"
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String, @Field("password") password: String
    ): LoginResponse

    @GET("/vendor")
    suspend fun getAllVendors(): AllVendorsResult

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
        @Header("Authorization") authHeader: String,
        @Part body: MultipartBody
    ): ProductData
}