package com.seefud.seefud.view.content.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seefud.seefud.R
import com.seefud.seefud.data.Result
import com.seefud.seefud.data.response.ProductData
import com.seefud.seefud.data.response.VendorData
import com.seefud.seefud.databinding.ActivityDetailBinding
import com.seefud.seefud.view.content.ProductAdapter
import com.seefud.seefud.view.content.home.HomeViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val scannedId = intent.getStringExtra(EXTRA_SCAN)
        val vendorId = intent.getIntExtra(EXTRA_VENDOR_ID, -1)
        val vendorName = intent.getStringExtra(EXTRA_VENDOR_NAME) ?: "Unknown"

        val idToUse = scannedId?.toIntOrNull() ?: vendorId

        idToUse.let { id ->
            // Observe the vendor details live data
            viewModel.getVendorById(id).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        // Show loading indicator if needed
                    }

                    is Result.Success -> {
                        result.data.let {
                            displayVendorDetails(it)
                            observeProducts(id)
                        }
                    }

                    is Result.Error -> {
                        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun observeProducts(vendorId: Int) {
        // Observe products live data
        viewModel.getProducts(vendorId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Show loading indicator if needed
                }

                is Result.Success -> {
                    displayProducts(result.data)
                }

                is Result.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayVendorDetails(vendor: VendorData) {
        binding.namarestoTxt.text = vendor.storename
    }

    private fun displayProducts(products: List<ProductData>) {
        val recyclerView: RecyclerView = findViewById(R.id.rv_makanan)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val foodAdapter = ProductAdapter(products)
        recyclerView.adapter = foodAdapter
    }

    companion object {
        const val EXTRA_VENDOR_ID = "extra_vendor_id"
        const val EXTRA_SCAN = "scannedId"
        const val EXTRA_VENDOR_NAME = "extra_vendor_name"
    }
}

