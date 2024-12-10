package com.seefud.seefud.data.api

import com.seefud.seefud.data.response.LoginResponse
import com.seefud.seefud.data.response.RegisterResponse
import com.seefud.seefud.data.response.VendorResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("/vendor")
    suspend fun getVendors(): VendorResponse

}