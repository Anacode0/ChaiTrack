package com.farmer.chaitrack.ui.notifications.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farmer.chaitrack.R
import com.farmer.chaitrack.databinding.ItemNotificationsBinding  // ← Changed
import com.farmer.chaitrack.domain.model.NotificationItem
import com.farmer.chaitrack.domain.usecase.notification.NotificationType
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class NotificationAdapter(
    private val onNotificationClick: (NotificationItem) -> Unit
) : ListAdapter<NotificationItem, NotificationAdapter.NotificationViewHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationsBinding.inflate(  // ← Changed
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding, onNotificationClick)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NotificationViewHolder(
        private val binding: ItemNotificationsBinding,  // ← Changed
        private val onNotificationClick: (NotificationItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: NotificationItem) {
            binding.apply {
                // Set icon based on type
                ivNotificationIcon.setImageResource(getIconForType(notification.type))

                // Set title and message
                tvTitle.text = notification.title
                tvMessage.text = notification.message

                // Set time (timestamp is Long, not Date)
                tvDeliveryTime.text = getTimeAgo(notification.timestamp)

                // Show/hide unread indicator
                viewUnreadIndicator.visibility = if (notification.isRead) View.GONE else View.VISIBLE

                // Change background if unread
                val context = root.context
                if (!notification.isRead) {
                    cardNotification.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.notification_unread_background)
                    )
                    tvTitle.setTextColor(ContextCompat.getColor(context, R.color.black))
                } else {
                    cardNotification.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.white)
                    )
                    tvTitle.setTextColor(ContextCompat.getColor(context, R.color.gray_dark))
                }

                // Click listener
                root.setOnClickListener {
                    onNotificationClick(notification)
                }
            }
        }

        private fun getIconForType(type: NotificationType): Int {
            return when (type) {
                NotificationType.DELIVERY -> R.drawable.ic_deliveries
                NotificationType.FERTILIZER -> R.drawable.ic_fertilizer
                NotificationType.FINANCE -> R.drawable.ic_finance
                NotificationType.SYSTEM -> R.drawable.ic_notifications
                NotificationType.GENERAL -> R.drawable.ic_notifications
                NotificationType.ANNOUNCEMENT -> R.drawable.ic_announcement
                NotificationType.REMINDER -> R.drawable.ic_reminder
            }
        }

        private fun getTimeAgo(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp

            return when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
                diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}m ago"
                diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}h ago"
                diff < TimeUnit.DAYS.toMillis(7) -> "${TimeUnit.MILLISECONDS.toDays(diff)}d ago"
                else -> {
                    val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
                    sdf.format(timestamp)
                }
            }
        }
    }

    class NotificationDiffCallback : DiffUtil.ItemCallback<NotificationItem>() {
        override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
            return oldItem == newItem
        }
    }
}