package com.seefud.seefud.view.content.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seefud.seefud.data.Result
import com.seefud.seefud.data.UserRepository
import com.seefud.seefud.data.pref.Vendor
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    private val _vendors = MutableLiveData<Result<List<Vendor>>>()
    val vendors: LiveData<Result<List<Vendor>>> get() = _vendors

    private val _vendorById = MutableLiveData<Vendor?>()
    val vendorById: LiveData<Vendor?> get() = _vendorById

    fun fetchVendors() {
        viewModelScope.launch {
            _vendors.postValue(Result.Loading)
            try {
                val response = repository.getVendors()
                _vendors.postValue(Result.Success(response))
            } catch (e: Exception) {
                _vendors.postValue(Result.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }

    fun getVendorById(vendorId: String) {
        viewModelScope.launch {
            val result = _vendors.value?.let { result ->
                if (result is Result.Success) {
                    result.data.find { it.id == vendorId }
                } else {
                    null
                }
            }
            _vendorById.postValue(result)
        }
    }
}
