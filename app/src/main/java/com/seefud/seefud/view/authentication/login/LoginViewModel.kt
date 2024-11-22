package com.seefud.seefud.view.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seefud.seefud.data.UserRepository
import com.seefud.seefud.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    suspend fun login(email: String, pass: String) = userRepository.login(email, pass)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }
}