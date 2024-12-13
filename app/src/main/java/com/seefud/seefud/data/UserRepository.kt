package com.seefud.seefud.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.seefud.seefud.data.api.ApiService
import com.seefud.seefud.data.pref.UserModel
import com.seefud.seefud.data.pref.UserPreference
import com.seefud.seefud.data.pref.VendorModel
import com.seefud.seefud.data.pref.VendorPreference
import com.seefud.seefud.data.response.AllVendorsResult
import com.seefud.seefud.data.response.LoginRequest
import com.seefud.seefud.data.response.LoginResult
import com.seefud.seefud.data.response.ProductData
import com.seefud.seefud.data.response.ProductResponse
import com.seefud.seefud.data.response.RegisterRequest
import com.seefud.seefud.data.response.RegisterResponse
import com.seefud.seefud.data.response.VendorData
import com.seefud.seefud.data.response.VendorDetailResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File


class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val vendorPreference: VendorPreference,
    private val apiService: ApiService
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun updateVendor(
        vendor: VendorModel, image: MultipartBody.Part? = null // Optional image for update
    ): VendorData {
        vendorPreference.saveVendor(vendor)

        val storeName = vendor.store_name.toRequestBody("text/plain".toMediaTypeOrNull())
        val description = vendor.description.toRequestBody("text/plain".toMediaTypeOrNull())
        val location = vendor.location.toRequestBody("text/plain".toMediaTypeOrNull())

        return apiService.updateVendor(
            vendor.id, storeName, description, location, image
        )
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getVendor(): Flow<VendorModel> {
        return vendorPreference.getVendor()
    }

    fun register(name: String, email: String, password: String): LiveData<Result<String?>> =
        liveData {
            emit(Result.Loading)
            try {
                val requestBody = RegisterRequest(name, email, password)
                val response = apiService.register(requestBody)
                val registerResult = response.message
                emit(Result.Success(registerResult!!))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = Gson().fromJson(errorBody, RegisterResponse::class.java).message
                emit(Result.Error(errorMessage ?: "Unknown error"))
            } catch (e: Exception) {
                emit(Result.Error("Unexpected error: ${e.message}"))
            }
        }

    fun login(email: String, password: String): LiveData<Result<LoginResult>> = liveData {
        emit(Result.Loading)
        try {
            val requestBody = LoginRequest(email, password)
            val response = apiService.login(requestBody)
            val loginResult = response.data

            emit(Result.Success(loginResult!!))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string() ?: "No error body"
            emit(Result.Error("Error ${e.code()}: $errorBody"))
        } catch (e: Exception) {
            emit(Result.Error("Unexpected error: ${e.message}"))
        }
    }

    fun getAllVendors(): LiveData<Result<List<VendorData>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllVendors()
            val vendors = response.listVendor
            emit(Result.Success(vendors))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = try {
                Gson().fromJson(jsonInString, AllVendorsResult::class.java)
            } catch (e: Exception) {
                null
            }
            val errorMessage =
                errorBody?.message ?: "An unexpected error occurred (HTTP ${e.code()})"
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    fun getDetailVendor(vendorId: Int): LiveData<Result<VendorData>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getVendorById(vendorId)
            val vendor = response.detailVendor
            emit(Result.Success(vendor))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, VendorDetailResult::class.java)
            val errorMessage = errorBody.message ?: "An unexpected error occurred"
            emit(Result.Error(errorMessage))
        }
    }

    fun getProducts(vendorId: Int): LiveData<Result<List<ProductData>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getVendorsProduct(vendorId)
            val product = response.listProduct
            emit(Result.Success(product))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ProductResponse::class.java)
            val errorMessage = errorBody.message ?: "An unexpected error occurred"
            emit(Result.Error(errorMessage))
        }
    }

    suspend fun createProduct(
        token: String,
        name: String,
        description: String,
        price: Int,
        qrCode: String,
        imageUri: File?
    ): ProductData {
        val requestBody =
            MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("name", name)
                .addFormDataPart("description", description)
                .addFormDataPart("price", price.toString()).addFormDataPart("qr_code", qrCode)

        imageUri?.let {
            val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
            requestBody.addFormDataPart("image", it.name, requestFile)
        }

        val requestBodyPart = requestBody.build()
        return apiService.createProduct("Bearer $token", requestBodyPart)
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            vendorPreference: VendorPreference,
            apiService: ApiService
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userPreference, vendorPreference, apiService)
        }.also { instance = it }
    }
}