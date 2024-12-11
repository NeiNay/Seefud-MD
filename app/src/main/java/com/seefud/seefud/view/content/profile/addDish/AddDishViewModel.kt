package com.seefud.seefud.view.content.profile.addDish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seefud.seefud.data.Result
import com.seefud.seefud.data.UserRepository
import com.seefud.seefud.data.response.ProductData
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.File

class AddDishViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _createProductResponse = MutableLiveData<Result<ProductData>>()
    val createProductResponse: LiveData<Result<ProductData>> = _createProductResponse

    fun saveDish(name: String, description: String, imageFile: File?) {
        viewModelScope.launch {
            _createProductResponse.value = Result.Loading
            try {
                // Get token from repository
                val userSession = userRepository.getSession().firstOrNull()
                val token = userSession?.token ?: throw Exception("User not logged in")

                // Prepare request body
                val price = 100 // Example price, replace with actual input
                val qrCode = "qr_code_example" // Replace with actual QR code if needed

                // Send API request (using multipart for image if available)
                val result =
                    userRepository.createProduct(token, name, description, price, qrCode, imageFile)

                // Handle API response
                _createProductResponse.value = Result.Success(result)
            } catch (e: Exception) {
                _createProductResponse.value = Result.Error(e.message ?: "An error occurred")
            }
        }
    }
}