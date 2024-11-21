package com.seefud.seefud.view.authentication

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.seefud.seefud.data.UserRepository
import com.seefud.seefud.data.di.Injection
import com.seefud.seefud.view.authentication.login.LoginViewModel
import com.seefud.seefud.view.authentication.signup.SignupViewModel
import com.seefud.seefud.view.authentication.welcome.WelcomeViewModel

class AuthViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthViewModelFactory? = null
        @JvmStatic
        fun getInstanceUser(context: Context): AuthViewModelFactory {
            if (INSTANCE == null) {
                synchronized(AuthViewModelFactory::class.java) {
                    INSTANCE = AuthViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as AuthViewModelFactory
        }
    }
}