package com.seefud.seefud.view.authentication.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.seefud.seefud.data.UserRepository
import com.seefud.seefud.data.pref.UserModel

class WelcomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}