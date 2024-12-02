package com.seefud.seefud.view.content.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seefud.seefud.data.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

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