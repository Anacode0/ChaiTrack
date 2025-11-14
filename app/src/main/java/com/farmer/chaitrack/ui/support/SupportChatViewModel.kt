package com.farmer.chaitrack.ui.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.chaitrack.domain.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SupportChatViewModel : ViewModel() {

    private val _state = MutableStateFlow(SupportChatState())
    val state: StateFlow<SupportChatState> = _state.asStateFlow()

    fun loadChatHistory() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // TODO: Replace with actual repository call
                kotlinx.coroutines.delay(800)

                val messages = getChatHistoryFromRepository()

                _state.update {
                    it.copy(
                        isLoading = false,
                        messages = messages
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load chat history"
                    )
                }
            }
        }
    }

    fun sendMessage(messageText: String) {
        viewModelScope.launch {
            _state.update { it.copy(isSending = true) }

            try {
                // Create new message
                val newMessage = ChatMessage(
                    id = System.currentTimeMillis().toString(),
                    message = messageText,
                    timestamp = System.currentTimeMillis(),  // ← Changed from Date() to Long
                    isSentByFarmer = true
                )

                // Add message to list immediately (optimistic update)
                val updatedMessages = _state.value.messages + newMessage
                _state.update {
                    it.copy(
                        messages = updatedMessages,
                        isSending = false
                    )
                }

                // TODO: Send message to repository/API
                kotlinx.coroutines.delay(500)

                _state.update { it.copy(messageSent = true) }

                // Simulate support response after a delay
                simulateSupportResponse()

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message ?: "Failed to send message",
                        isSending = false
                    )
                }
            }
        }
    }

    fun resetMessageSentState() {
        _state.update { it.copy(messageSent = false) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    // TODO: Replace with actual repository call
    private fun getChatHistoryFromRepository(): List<ChatMessage> {
        val currentTime = System.currentTimeMillis()

        // Sample data
        return listOf(
            ChatMessage(
                id = "1",
                message = "I have an issue with my delivery from today.",
                timestamp = currentTime - 3600000L,  // 1 hour ago
                isSentByFarmer = true
            ),
            ChatMessage(
                id = "2",
                message = "Hello! We are looking into it.",
                timestamp = currentTime - 3540000L,  // 59 minutes ago
                isSentByFarmer = false
            ),
            ChatMessage(
                id = "3",
                message = "Can you provide more details about the issue?",
                timestamp = currentTime - 3480000L,  // 58 minutes ago
                isSentByFarmer = false
            )
        )
    }

    private fun simulateSupportResponse() {
        viewModelScope.launch {
            // Show typing indicator
            _state.update { it.copy(isTyping = true) }

            // Simulate delay for support response
            kotlinx.coroutines.delay(2000)

            val supportMessage = ChatMessage(
                id = System.currentTimeMillis().toString(),
                message = "Thank you for reaching out. Our team will review your message and get back to you shortly.",
                timestamp = System.currentTimeMillis(),  // ← Changed from Date() to Long
                isSentByFarmer = false
            )

            val updatedMessages = _state.value.messages + supportMessage
            _state.update {
                it.copy(
                    messages = updatedMessages,
                    isTyping = false
                )
            }
        }
    }
}