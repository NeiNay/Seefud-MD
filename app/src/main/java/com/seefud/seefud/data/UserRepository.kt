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
import com.seefud.seefud.data.response.LoginResponse
import com.seefud.seefud.data.response.LoginResult
import com.seefud.seefud.data.response.ProductData
import com.seefud.seefud.data.response.ProductResponse
import com.seefud.seefud.data.response.RegisterResponse
import com.seefud.seefud.data.response.VendorData
import com.seefud.seefud.data.response.VendorDetailResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
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

    suspend fun saveVendor(vendor: VendorModel) {
        vendorPreference.saveVendor(vendor)
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
                val message = apiService.register(name, email, password).message
                emit(Result.Success(message))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error(errorMessage!!))
            }
        }

    fun login(email: String, password: String): LiveData<Result<LoginResult>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.login(email, password).data
            val id = result?.userId
            val token = result?.token
            emit(Result.Success(LoginResult(id, token)))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage =
                errorBody.message ?: "Default error message\nAn unexpected error occurred"
            emit(Result.Error(errorMessage))
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
            val errorBody = Gson().fromJson(jsonInString, AllVendorsResult::class.java)
            val errorMessage = errorBody.message ?: "An unexpected error occurred"
            emit(Result.Error(errorMessage))
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

        // Make the POST request with the Authorization header
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