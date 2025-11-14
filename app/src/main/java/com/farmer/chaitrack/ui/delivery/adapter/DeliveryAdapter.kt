package com.farmer.chaitrack.ui.delivery.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farmer.chaitrack.databinding.ItemDeliveryBinding
import com.farmer.chaitrack.domain.model.DeliveryItem
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
// import java.time.format.DateTimeFormatter

class DeliveryAdapter : ListAdapter<DeliveryItem, DeliveryAdapter.DeliveryViewHolder>(DeliveryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding = ItemDeliveryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DeliveryViewHolder(
        private val binding: ItemDeliveryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        private val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 1
        }

        fun bind(delivery: DeliveryItem) {
            binding.apply {
                tvDeliveryDate.text = dateFormat.format(delivery.date)
                tvDeliveryTime.text = delivery.time
                tvDeliveryWeight.text = "${numberFormat.format(delivery.weight)} kg"
                tvDeliveryClerk.text = delivery.clerkName
            }
        }
    }

    class DeliveryDiffCallback : DiffUtil.ItemCallback<DeliveryItem>() {
        override fun areItemsTheSame(oldItem: DeliveryItem, newItem: DeliveryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DeliveryItem, newItem: DeliveryItem): Boolean {
            return oldItem == newItem
        }
    }
}