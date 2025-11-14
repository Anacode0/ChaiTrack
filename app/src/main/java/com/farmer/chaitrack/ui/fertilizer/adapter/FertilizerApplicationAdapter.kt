package com.farmer.chaitrack.ui.fertilizer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farmer.chaitrack.R
import com.farmer.chaitrack.databinding.ItemFertilizerApplicationBinding
import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.ui.fertilizer.ApplicationStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FertilizerApplicationAdapter :
    ListAdapter<FertilizerApplication, FertilizerApplicationAdapter.ApplicationViewHolder>(ApplicationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        val binding = ItemFertilizerApplicationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ApplicationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ApplicationViewHolder(
        private val binding: ItemFertilizerApplicationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(application: FertilizerApplication) {
            binding.apply {
                // Season
                tvSeason.text = application.season

                // Format: "Plot Size: 2.5 acres"
                tvPlotSize.text = root.context.getString(
                    R.string.plot_size_format,
                    application.plotSize
                )

                // Format: "Quantity: 5 bags"
                tvQuantity.text = root.context.getString(
                    R.string.quantity_format,
                    application.quantityRequested
                )

                // Format date: "Applied: 15 Oct 2024"
                val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                tvDateApplied.text = root.context.getString(
                    R.string.applied_date_format,
                    dateFormat.format(Date(application.applicationDate))
                )

                // Set status with appropriate styling
                tvStatus.text = getStatusText(root.context, application.status)

                // Use a rounded background drawable instead of just background color
                val statusBadge = when (application.status) {
                    ApplicationStatus.COMPLETED -> R.drawable.bg_status_completed
                    ApplicationStatus.APPROVED -> R.drawable.bg_status_approved
                    ApplicationStatus.REJECTED -> R.drawable.bg_status_rejected
                    ApplicationStatus.PENDING_REVIEW -> R.drawable.bg_status_pending
                    ApplicationStatus.UNDER_REVIEW -> R.drawable.bg_status_pending
                    ApplicationStatus.READY_FOR_PICKUP -> R.drawable.bg_status_approved
                    ApplicationStatus.PICKED_UP -> R.drawable.bg_status_approved
                }
                tvStatus.setBackgroundResource(statusBadge)
            }
        }

        private fun getStatusText(context: Context, status: ApplicationStatus): String {
            return when (status) {
                ApplicationStatus.PENDING_REVIEW -> context.getString(R.string.pending_review)
                ApplicationStatus.UNDER_REVIEW -> context.getString(R.string.under_review)
                ApplicationStatus.APPROVED -> context.getString(R.string.approved)
                ApplicationStatus.READY_FOR_PICKUP -> context.getString(R.string.ready_for_pickup)
                ApplicationStatus.PICKED_UP -> context.getString(R.string.picked_up)
                ApplicationStatus.REJECTED -> context.getString(R.string.rejected)
                ApplicationStatus.COMPLETED -> context.getString(R.string.completed)
            }
        }

        private fun getStatusColor(context: Context, status: ApplicationStatus): Int {
            return when (status) {
                ApplicationStatus.PENDING_REVIEW -> ContextCompat.getColor(context, R.color.orange)
                ApplicationStatus.UNDER_REVIEW -> ContextCompat.getColor(context, R.color.warning_orange)
                ApplicationStatus.APPROVED -> ContextCompat.getColor(context, R.color.success_green)
                ApplicationStatus.READY_FOR_PICKUP -> ContextCompat.getColor(context, R.color.green_dark)
                ApplicationStatus.PICKED_UP -> ContextCompat.getColor(context, R.color.success_green)
                ApplicationStatus.REJECTED -> ContextCompat.getColor(context, R.color.red)
                ApplicationStatus.COMPLETED -> ContextCompat.getColor(context, R.color.gray_dark)
            }
        }
    }

    class ApplicationDiffCallback : DiffUtil.ItemCallback<FertilizerApplication>() {
        override fun areItemsTheSame(
            oldItem: FertilizerApplication,
            newItem: FertilizerApplication
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FertilizerApplication,
            newItem: FertilizerApplication
        ): Boolean {
            return oldItem == newItem
        }
    }
}