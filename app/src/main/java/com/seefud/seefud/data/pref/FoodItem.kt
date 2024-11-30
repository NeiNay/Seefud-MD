package com.seefud.seefud.data.pref

data class FoodItem(
    val name: String? = null,
    val description: String? = null,
    val imageUrl: String? = null
)

data class Vendor(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val profileImageUrl: String? = null,
    val imageUrl: String? = null,
    val contactInfo: String? = null,
    val location: String? = null,
    val foods: List<FoodItem>? = null
)
