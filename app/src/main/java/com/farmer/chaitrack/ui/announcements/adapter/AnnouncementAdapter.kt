package com.farmer.chaitrack.ui.announcements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farmer.chaitrack.databinding.ItemAnnouncementBinding
import com.farmer.chaitrack.domain.model.Announcement
import java.text.SimpleDateFormat
import java.util.*

class AnnouncementAdapter : ListAdapter<Announcement, AnnouncementAdapter.AnnouncementViewHolder>(
    AnnouncementDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val binding = ItemAnnouncementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnnouncementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AnnouncementViewHolder(
        private val binding: ItemAnnouncementBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var isExpanded = false

        fun bind(announcement: Announcement) {
            binding.apply {
                tvTitle.text = announcement.title
                tvDeliveryDate.text = formatDate(announcement.date)
                tvBodyPreview.text = announcement.content
                tvBodyFull.text = announcement.content

                isExpanded = false
                updateExpandedState()

                btnReadMore.setOnClickListener {
                    isExpanded = true
                    updateExpandedState()
                }

                btnShowLess.setOnClickListener {
                    isExpanded = false
                    updateExpandedState()
                }
            }
        }

        private fun updateExpandedState() {
            binding.apply {
                if (isExpanded) {
                    tvBodyPreview.visibility = View.GONE
                    tvBodyFull.visibility = View.VISIBLE
                    btnReadMore.visibility = View.GONE
                    btnShowLess.visibility = View.VISIBLE
                } else {
                    tvBodyPreview.visibility = View.VISIBLE
                    tvBodyFull.visibility = View.GONE
                    btnReadMore.visibility = View.VISIBLE
                    btnShowLess.visibility = View.GONE
                }
            }
        }

        private fun formatDate(dateString: String): String {
            return try {
                val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date = isoFormat.parse(dateString)
                val displayFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                "Posted on: ${displayFormat.format(date ?: Date())}"
            } catch (e: Exception) {
                "Posted on: $dateString"
            }
        }
    }

    private class AnnouncementDiffCallback : DiffUtil.ItemCallback<Announcement>() {
        override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem == newItem
        }
    }
}