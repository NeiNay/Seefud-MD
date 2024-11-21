package com.seefud.seefud.data.di

import android.content.Context
import com.seefud.seefud.data.UserRepository
import com.seefud.seefud.data.api.ApiConfig
import com.seefud.seefud.data.pref.UserPreference
import com.seefud.seefud.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }
}