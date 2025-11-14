package com.farmer.chaitrack.ui.support

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmer.chaitrack.R
import com.farmer.chaitrack.databinding.ActivitySupportChatBinding
import com.farmer.chaitrack.ui.support.adapter.ChatMessageAdapter
import com.farmer.chaitrack.ui.support.*
import kotlinx.coroutines.launch

class SupportChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupportChatBinding
    private val viewModel: SupportChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupMessageInput()
        observeState()

        // Load chat history
        viewModel.loadChatHistory()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatMessageAdapter()
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(this@SupportChatActivity).apply {
                // Reverse layout so new messages appear at bottom
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }

    private fun setupMessageInput() {
        binding.btnSend.setOnClickListener {
            sendMessage()
        }

        // Optional: Send on Enter key
        binding.etMessage.setOnEditorActionListener { _, _, _ ->
            sendMessage()
            true
        }
    }

    private fun sendMessage() {
        val message = binding.etMessage.text?.toString()?.trim()

        if (message.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.empty_message), Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.sendMessage(message)
        binding.etMessage.text?.clear()

        // Scroll to bottom after sending
        binding.rvMessages.scrollToPosition(chatAdapter.itemCount - 1)
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    updateUI(state)
                }
            }
        }
    }

    private fun updateUI(state: SupportChatState) {
        // Show/Hide loading
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

        // Update messages list
        if (state.messages.isEmpty() && !state.isLoading) {
            binding.emptyState.visibility = View.VISIBLE
            binding.rvMessages.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.rvMessages.visibility = View.VISIBLE
            chatAdapter.submitList(state.messages)

            // Scroll to bottom when new messages arrive
            if (state.messages.isNotEmpty()) {
                binding.rvMessages.scrollToPosition(state.messages.size - 1)
            }
        }

        // Handle message sent
        if (state.messageSent) {
            Toast.makeText(this, getString(R.string.message_sent), Toast.LENGTH_SHORT).show()
            viewModel.resetMessageSentState()
        }

        // Handle errors
        state.error?.let { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }
}