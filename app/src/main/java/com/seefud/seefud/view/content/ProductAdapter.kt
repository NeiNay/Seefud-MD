package com.seefud.seefud.view.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seefud.seefud.data.response.ProductData
import com.seefud.seefud.databinding.ItemProdukBinding

class ProductAdapter(private val products: List<ProductData>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProdukBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(private val binding: ItemProdukBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductData) {
            binding.tvFoodName.text = product.name
            binding.tvFoodDescription.text = product.description
        }
    }
}