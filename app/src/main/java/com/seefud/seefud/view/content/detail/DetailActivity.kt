package com.seefud.seefud.view.content.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seefud.seefud.R
import com.seefud.seefud.data.pref.FoodItem
import com.seefud.seefud.databinding.ActivityDetailBinding
import com.seefud.seefud.view.content.FoodAdapter

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val scannedId = intent.getStringExtra("scannedId")

//        if (scannedId != null) {
//            fetchVendorData(scannedId)
//        }

        val recyclerView: RecyclerView = findViewById(R.id.rv_makanan)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        // Sample data for the RecyclerView
        val foodList = listOf(
            FoodItem(
                "Nasi Goreng", "Delicious fried rice with spices", ""
            ), FoodItem(
                "Sate Ayam", "Grilled chicken skewers with peanut sauce", ""
            ), FoodItem(
                "Rendang", "Spicy beef dish with coconut milk", ""
            )
        )


        val adapter = FoodAdapter(foodList)
        recyclerView.adapter = adapter

    }

//    private fun fetchVendorData(scannedId: String) {
//        //logika ambil data vendor pake Id
//        val vendor = getVendorById(scannedId)
//        if (vendor != null) {
//            displayVendorDisplay(vendor)
//        }
//    }
//
//    private fun displayVendorDisplay(vendor: Vendor) {
//        binding.namarestoTxt.text = vendor.name
//        Glide.with(this).load(vendor.imageUrl).into(binding.ivResto)
//        Glide.with(this).load(vendor.profileImageUrl).into(binding.profileIv)
//
//        // Set up RecyclerView for the food items
//        val foodAdapter = FoodAdapter(vendor.foods)
//        binding.rvMakanan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding.rvMakanan.adapter = foodAdapter
//    }

    companion object {
        const val EXTRA_VENDOR = "extra_vendor"
    }

}