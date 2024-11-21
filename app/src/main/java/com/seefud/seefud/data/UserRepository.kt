package com.seefud.seefud.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.seefud.seefud.data.api.ApiService
import com.seefud.seefud.data.pref.UserModel
import com.seefud.seefud.data.pref.UserPreference
import com.seefud.seefud.data.response.LoginResult
import com.seefud.seefud.data.response.RegisterResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun register(name: String, email: String, password: String): LiveData<Result<String?>> = liveData  {
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

    fun login(email: String, password: String): LiveData<Result<LoginResult>> = liveData  {
        emit(Result.Loading)
        try {
            val result = apiService.login(email, password).loginResult
            val name = result?.name
            val id = result?.userId
            val token = result?.token
            emit(Result.Success(LoginResult(name, id, token)))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage!!))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}