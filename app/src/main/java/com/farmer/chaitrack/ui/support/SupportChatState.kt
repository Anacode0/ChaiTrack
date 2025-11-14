package com.farmer.chaitrack.ui.support

import com.farmer.chaitrack.domain.model.ChatMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * UI State for the Support Chat screen
 */
data class SupportChatState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val messageText: String = "",
    val isSending: Boolean = false,
    val isTyping: Boolean = false,
    val error: String? = null,
    val isEmpty: Boolean = true,
    val isConnected: Boolean = true,
    val canSendMessage: Boolean = true,
    val attachedFiles: List<AttachedFile> = emptyList(),
    val maxFileSize: Long = 5 * 1024 * 1024, // 5MB
    val supportAgentName: String? = null,
    val supportAgentStatus: SupportAgentStatus = SupportAgentStatus.OFFLINE,
    val messageSent: Boolean = false
) {
    /**
     * Checks if message can be sent
     */
    fun canSend(): Boolean {
        return messageText.isNotBlank() &&
                !isSending &&
                isConnected &&
                canSendMessage
    }

    /**
     * Checks if files can be attached
     */
    fun canAttachFiles(): Boolean {
        return attachedFiles.size < 5 && !isSending
    }

    /**
     * Gets the character count for input field
     */
    fun getCharacterCount(): String {
        return "${messageText.length}/500"
    }

    /**
     * Checks if character limit is reached
     */
    fun isCharacterLimitReached(): Boolean {
        return messageText.length >= 500
    }

    /**
     * Checks if the state represents an error scenario
     */
    fun hasError(): Boolean {
        return error != null
    }

    /**
     * Gets the empty state message
     */
    fun getEmptyStateMessage(): String {
        return when {
            !isConnected -> "No internet connection"
            error != null -> error
            else -> "No messages yet. Start a conversation with support."
        }
    }

    /**
     * Gets the status text for support agent
     */
    fun getSupportAgentStatusText(): String {
        return when (supportAgentStatus) {
            SupportAgentStatus.ONLINE -> "Online"
            SupportAgentStatus.AWAY -> "Away"
            SupportAgentStatus.BUSY -> "Busy"
            SupportAgentStatus.OFFLINE -> "Offline"
        }
    }

    /**
     * Gets the typing indicator text
     */
    fun getTypingIndicatorText(): String {
        return if (supportAgentName != null) {
            "$supportAgentName is typing..."
        } else {
            "Support is typing..."
        }
    }

    /**
     * Groups messages by date for display
     */
    fun getGroupedMessages(): Map<String, List<ChatMessage>> {
        val now = System.currentTimeMillis()
        val oneDayMs = 24 * 60 * 60 * 1000L
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        val grouped = mutableMapOf<String, MutableList<ChatMessage>>()

        messages.forEach { message ->
            val age = now - message.timestamp
            val key = when {
                age < oneDayMs -> "Today"
                age < 2 * oneDayMs -> "Yesterday"
                else -> dateFormat.format(Date(message.timestamp))
            }
            grouped.getOrPut(key) { mutableListOf() }.add(message)
        }

        return grouped
    }

    /**
     * Gets the most recent message
     */
    fun getLastMessage(): ChatMessage? {
        return messages.lastOrNull()
    }

    /**
     * Checks if the last message was from user
     */
    fun isLastMessageFromUser(): Boolean {
        return getLastMessage()?.isSentByFarmer == true
    }
}

/**
 * Support agent status
 */
enum class SupportAgentStatus {
    ONLINE,
    AWAY,
    BUSY,
    OFFLINE
}

/**
 * Attached file data
 */
data class AttachedFile(
    val id: String,
    val name: String,
    val size: Long,
    val mimeType: String,
    val uri: String,
    val isUploading: Boolean = false,
    val uploadProgress: Int = 0,
    val error: String? = null
) {
    /**
     * Gets formatted file size
     */
    fun getFormattedSize(): String {
        return when {
            size < 1024 -> "$size B"
            size < 1024 * 1024 -> "${"%.2f".format(size / 1024.0)} KB"
            else -> "${"%.2f".format(size / (1024.0 * 1024.0))} MB"
        }
    }

    /**
     * Checks if file is an image
     */
    fun isImage(): Boolean {
        return mimeType.startsWith("image/")
    }

    /**
     * Checks if file is a document
     */
    fun isDocument(): Boolean {
        return mimeType.startsWith("application/") ||
                mimeType == "text/plain"
    }
}

/**
 * Events that can occur on the Support Chat screen
 */
sealed class SupportChatEvent {
    object LoadMessages : SupportChatEvent()
    object RefreshMessages : SupportChatEvent()
    data class SendMessage(val text: String) : SupportChatEvent()
    data class UpdateMessageText(val text: String) : SupportChatEvent()
    data class AttachFile(val uri: String, val fileName: String, val fileSize: Long, val mimeType: String) : SupportChatEvent()
    data class RemoveAttachedFile(val fileId: String) : SupportChatEvent()
    object SendWithAttachments : SupportChatEvent()
    object MarkMessagesAsRead : SupportChatEvent()
    data class DeleteMessage(val messageId: String) : SupportChatEvent()
    data class CopyMessageText(val text: String) : SupportChatEvent()
    object ScrollToBottom : SupportChatEvent()
    object StartTyping : SupportChatEvent()
    object StopTyping : SupportChatEvent()
    object ConnectToSupport : SupportChatEvent()
    object DisconnectFromSupport : SupportChatEvent()
    data class RetryMessage(val messageId: String) : SupportChatEvent()
    object RetryLoading : SupportChatEvent()
    object DismissError : SupportChatEvent()
}

/**
 * Side effects for the Support Chat screen
 */
sealed class SupportChatSideEffect {
    data class ShowToast(val message: String) : SupportChatSideEffect()
    object ScrollToBottom : SupportChatSideEffect()
    object ClearInput : SupportChatSideEffect()
    object HideKeyboard : SupportChatSideEffect()
    object ShowFilePicker : SupportChatSideEffect()
    data class CopyToClipboard(val text: String) : SupportChatSideEffect()
    data class ShowFileSizeError(val maxSize: String) : SupportChatSideEffect()
    object ShowConnectionError : SupportChatSideEffect()
    object VibrateForNewMessage : SupportChatSideEffect()
    object PlayNotificationSound : SupportChatSideEffect()
    data class ShowConfirmationDialog(
        val title: String,
        val message: String,
        val onConfirm: () -> Unit
    ) : SupportChatSideEffect()
}

/**
 * Message delivery status
 */
enum class MessageDeliveryStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ,
    FAILED
}

/**
 * Chat session info
 */
data class ChatSessionInfo(
    val sessionId: String,
    val startedAt: Long,
    val agentId: String?,
    val agentName: String?,
    val isActive: Boolean,
    val queuePosition: Int? = null
) {
    /**
     * Gets session duration in minutes
     */
    fun getSessionDurationMinutes(): Long {
        val duration = System.currentTimeMillis() - startedAt
        return duration / (60 * 1000)
    }

    /**
     * Gets queue position text
     */
    fun getQueuePositionText(): String? {
        return queuePosition?.let {
            "You are #$it in queue"
        }
    }
}