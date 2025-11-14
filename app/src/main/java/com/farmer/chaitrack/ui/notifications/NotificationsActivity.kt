package com.farmer.chaitrack.ui.notifications

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmer.chaitrack.databinding.ActivityNotificationsBinding
import com.farmer.chaitrack.ui.notifications.adapter.NotificationAdapter
import kotlinx.coroutines.launch

class NotificationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private val viewModel: NotificationsViewModel by viewModels()
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupMarkAllRead()
        observeState()

        // Handle back button
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        // Load notifications
        viewModel.loadNotifications()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        notificationAdapter = NotificationAdapter(
            onNotificationClick = { notification ->
                viewModel.markAsRead(notification.id)
            }
        )

        binding.rvNotifications.apply {
            layoutManager = LinearLayoutManager(this@NotificationsActivity)
            adapter = notificationAdapter
        }
    }

    private fun setupMarkAllRead() {
        binding.tvMarkAllRead.setOnClickListener {
            viewModel.markAllAsRead()
        }
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

    private fun updateUI(state: NotificationsState) {
        // Show/Hide loading
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

        // Show/Hide mark all read button
        binding.tvMarkAllRead.visibility =
            if (state.notifications.any { !it.isRead }) View.VISIBLE else View.GONE

        // Update notifications list
        if (state.notifications.isEmpty() && !state.isLoading) {
            binding.emptyState.visibility = View.VISIBLE
            binding.rvNotifications.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.rvNotifications.visibility = View.VISIBLE
            notificationAdapter.submitList(state.notifications)
        }

        // Handle errors
        state.error?.let { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    override fun onPause() {
        super.onPause()
        // Mark all notifications as read when leaving the screen
        viewModel.markAllAsRead()
    }
}