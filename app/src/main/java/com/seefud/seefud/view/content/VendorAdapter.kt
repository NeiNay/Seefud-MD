package com.seefud.seefud.view.content

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seefud.seefud.R
import com.seefud.seefud.data.pref.Vendor
import com.seefud.seefud.databinding.ItemVendorBinding
import com.seefud.seefud.view.content.detail.DetailActivity

class VendorAdapter : ListAdapter<Vendor, VendorAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVendorBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val vendorItem = getItem(position)
        holder.bind(vendorItem)

        holder.itemView.setOnClickListener { view ->
            view.context.startActivity(
                Intent(view.context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_VENDOR, vendorItem.id)
                }
            )
        }
    }

    class MyViewHolder(private val binding: ItemVendorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(vendor: Vendor) {
            with(binding) {
                tvVendorName.text = vendor.name
                tvVendorDescription.text = vendor.description
                Glide.with(itemView.context)
                    .load(vendor.imageUrl)
                    .placeholder(R.drawable.plate)
                    .into(ivVendorImage)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Vendor>() {
            override fun areItemsTheSame(oldItem: Vendor, newItem: Vendor): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Vendor, newItem: Vendor
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}