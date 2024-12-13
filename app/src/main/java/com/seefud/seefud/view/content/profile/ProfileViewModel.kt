package com.seefud.seefud.view.content.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.seefud.seefud.data.Result
import com.seefud.seefud.data.UserRepository
import com.seefud.seefud.data.pref.UserModel
import com.seefud.seefud.data.pref.VendorModel
import com.seefud.seefud.data.response.ProductData
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()
    fun getVendor(): LiveData<VendorModel> = repository.getVendor().asLiveData()

    var currentImageUri: Uri? = null
    private val _createProductResponse = MutableLiveData<Result<ProductData>>()

    val createProductResponse: LiveData<Result<ProductData>> = _createProductResponse

    fun uploadDish(name: String, description: String, imageFile: File?) {
        viewModelScope.launch {
            _createProductResponse.value = Result.Loading
            try {
                val userSession =
                    repository.getSession().firstOrNull() ?: throw Exception("User not logged in")
                val token = userSession.token

                val price = 0
                val qrCode = ""

                val result =
                    repository.createProduct(token, name, description, price, qrCode, imageFile)

                _createProductResponse.value = Result.Success(result)
            } catch (e: Exception) {
                _createProductResponse.value = Result.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun updateVendor(vendor: VendorModel) {
        viewModelScope.launch {
            repository.updateVendor(vendor, null)
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