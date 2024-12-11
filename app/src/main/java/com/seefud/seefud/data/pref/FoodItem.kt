package com.seefud.seefud.data.pref

data class FoodItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int = 0
)
