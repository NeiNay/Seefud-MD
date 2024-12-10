package com.seefud.seefud.view.content.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seefud.seefud.R
import com.seefud.seefud.data.pref.FoodItem
import com.seefud.seefud.data.pref.Vendor
import com.seefud.seefud.databinding.ActivityDetailBinding
import com.seefud.seefud.view.content.FoodAdapter
import com.seefud.seefud.view.content.home.HomeViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val scannedId = intent.getStringExtra("scannedId")
        val vendorId = intent.getStringExtra(EXTRA_VENDOR)

        val idToUse = scannedId ?: vendorId

        idToUse?.let { id ->
            viewModel.getVendorById(id)
            viewModel.vendorById.observe(this) { vendor ->
                vendor?.let {
                    displayVendorDetails(it)
                } ?: run {
                    Toast.makeText(this, "Vendor not found", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val recyclerView: RecyclerView = findViewById(R.id.rv_makanan)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        // Sample data
        val foodList = listOf(
            FoodItem(
                "Nasi Goreng", "Delicious fried rice with spices", ""
            ),
            FoodItem(
                "Sate Ayam", "Grilled chicken skewers with peanut sauce", ""
            ),
            FoodItem(
                "Rendang", "Spicy beef dish with coconut milk", ""
            )
        )

        val foodAdapter = FoodAdapter(foodList)
        recyclerView.adapter = foodAdapter
    }

    private fun displayVendorDetails(vendor: Vendor) {
        binding.namarestoTxt.text = vendor.name
        Glide.with(this).load(vendor.imageUrl).into(binding.ivResto)
        Glide.with(this).load(vendor.profileImageUrl).into(binding.profileIv)
    }

    companion object {
        const val EXTRA_VENDOR = "extra_vendor"
    }

}