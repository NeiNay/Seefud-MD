package com.seefud.seefud.view.content

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seefud.seefud.data.response.VendorData
import com.seefud.seefud.databinding.ItemVendorBinding
import com.seefud.seefud.view.content.detail.DetailActivity

class VendorAdapter : ListAdapter<VendorData, VendorAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVendorBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val vendorItem = getItem(position)
        holder.bind(vendorItem)

        holder.itemView.setOnClickListener { view ->
            view.context.startActivity(Intent(view.context, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_VENDOR_ID, vendorItem.id)
                putExtra(DetailActivity.EXTRA_VENDOR_NAME, vendorItem.storename)
            })
        }
    }

    class MyViewHolder(private val binding: ItemVendorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(vendor: VendorData) {
            with(binding) {
                tvVendorName.text = vendor.storename
                tvVendorDescription.text = vendor.description
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VendorData>() {
            override fun areItemsTheSame(oldItem: VendorData, newItem: VendorData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: VendorData, newItem: VendorData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}