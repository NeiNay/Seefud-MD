package com.seefud.seefud.view.content.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {
    private val _scannedId = MutableLiveData<String>()
    val scannedId: LiveData<String> = _scannedId

    fun setScannedId(id: String) {
        _scannedId.value = id
    }
}
