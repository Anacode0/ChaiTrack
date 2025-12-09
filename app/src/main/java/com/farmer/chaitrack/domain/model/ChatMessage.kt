package com.farmer.chaitrack.domain.model

data class ChatMessage(
    val id: String,
    val message: String,
    val timestamp: Long,
    val isSentByFarmer: Boolean,
    val senderId: String? = null,
    val senderName: String? = null,
    val status: MessageStatus = MessageStatus.SENT,
    val attachments: List<MessageAttachment> = emptyList(),
    val isRead: Boolean = false,
    val readAt: Long? = null
) {
    fun getFormattedDate(): String {
        val date = java.util.Date(timestamp)
        val format = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        return format.toString()
    }

    fun getFormattedTime(): String {
        val date = java.util.Date(timestamp)
        val format = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
        return format.format(date)
    }

    fun hasAttachments(): Boolean {
        return attachments.isNotEmpty()
    }

    fun isFromSupport(): Boolean {
        return !isSentByFarmer
    }

    fun hasFailed(): Boolean {
        return status == MessageStatus.FAILED
    }

    fun isSending(): Boolean {
        return status == MessageStatus.SENDING
    }
}

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ,
    FAILED
}

data class MessageAttachment(
    val id: String,
    val fileName: String,
    val fileSize: Long,
    val mimeType: String,
    val url: String,
    val thumbnailUrl: String? = null
) {
    fun isImage(): Boolean {
        return mimeType.startsWith("image/")
    }

    fun isDocument(): Boolean {
        return mimeType.startsWith("application/") || mimeType == "text/plain"
    }

    fun getFormattedSize(): String {
        return when {
            fileSize < 1024 -> "$fileSize B"
            fileSize < 1024 * 1024 -> "${"%.2f".format(fileSize / 1024.0)} KB"
            else -> "${"%.2f".format(fileSize / (1024.0 * 1024.0))} MB"
        }
    }
}