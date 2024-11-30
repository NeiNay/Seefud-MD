package com.seefud.seefud.view.content

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seefud.seefud.R
import com.seefud.seefud.data.pref.FoodItem

class FoodAdapter(private val foodList: List<FoodItem>) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodImage: ImageView = view.findViewById(R.id.iv_food_image)
        val foodName: TextView = view.findViewById(R.id.tv_food_name)
        val foodDescription: TextView = view.findViewById(R.id.tv_food_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_makanan, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = foodList[position]
        holder.foodName.text = foodItem.name
        holder.foodDescription.text = foodItem.description
        Glide.with(holder.foodImage.context).load(foodItem.imageUrl).placeholder(R.drawable.plate)
            .into(holder.foodImage)
    }

    override fun getItemCount(): Int = foodList.size
}
