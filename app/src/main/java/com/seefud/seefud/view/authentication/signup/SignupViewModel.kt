package com.seefud.seefud.view.authentication.signup

import androidx.lifecycle.ViewModel
import com.seefud.seefud.data.UserRepository

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, pass: String) =
        userRepository.register(name, email, pass)
}