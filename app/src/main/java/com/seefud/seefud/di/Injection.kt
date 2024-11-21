package com.seefud.seefud.di

import android.content.Context
import com.seefud.seefud.data.UserRepository
import com.seefud.seefud.data.pref.UserPreference
import com.seefud.seefud.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}