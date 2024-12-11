package com.seefud.seefud.view.content.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.seefud.seefud.data.Result
import com.seefud.seefud.data.UserRepository
import com.seefud.seefud.data.response.ProductData
import com.seefud.seefud.data.response.VendorData

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    fun fetchVendors(): LiveData<Result<List<VendorData>>> = repository.getAllVendors()
    fun getVendorById(id: Int): LiveData<Result<VendorData>> = repository.getDetailVendor(id)
    fun getProducts(id: Int): LiveData<Result<List<ProductData>>> = repository.getProducts(id)
}
