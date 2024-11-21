package com.seefud.seefud.view.content.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seefud.seefud.data.UserRepository
import com.seefud.seefud.data.pref.UserModel
import kotlinx.coroutines.launch

class SplashViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _userSession = MutableLiveData<UserModel>()
    val userSession: LiveData<UserModel> get() = _userSession

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            // Collecting the user session flow from the repository
            userRepository.getSession().collect { user ->
                _userSession.postValue(user)
            }
        }
    }
}
