package com.seefud.seefud.view.content.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.seefud.seefud.data.UserRepository
import com.seefud.seefud.data.pref.UserModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()

    fun updateUserProfile(name: String, email: String) {
        viewModelScope.launch {
            // Update user info in repository (database, API, etc.)
//            repository.updateUserProfile(name, email)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

//    fun deleteAccount() {
//        viewModelScope.launch {
//            repository.delete()
//        }
//    }
}