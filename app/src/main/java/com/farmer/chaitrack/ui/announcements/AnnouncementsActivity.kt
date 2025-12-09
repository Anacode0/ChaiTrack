package com.farmer.chaitrack.ui.announcements

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmer.chaitrack.databinding.ActivityAnnouncementsBinding
import com.farmer.chaitrack.domain.model.Announcement
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AnnouncementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnnouncementsBinding
    private val viewModel: AnnouncementsViewModel by viewModels()
    private lateinit var announcementAdapter: AnnouncementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnnouncementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupObservers()
        loadAnnouncements()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        announcementAdapter = AnnouncementAdapter()

        binding.rvAnnouncements.apply {
            layoutManager = LinearLayoutManager(this@AnnouncementsActivity)
            adapter = announcementAdapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                handleState(state)
            }
        }
    }

    private fun loadAnnouncements() {
        viewModel.loadAnnouncements()
    }

    private fun handleState(state: AnnouncementsState) {
        handleLoading(state.isLoading)
        handleAnnouncements(state.announcements, state.isLoading)
        handleError(state.error)
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handleAnnouncements(announcements: List<Announcement>, isLoading: Boolean) {
        val isEmpty = announcements.isEmpty() && !isLoading

        if (isEmpty) {
            binding.emptyState.visibility = View.VISIBLE
            binding.rvAnnouncements.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.rvAnnouncements.visibility = View.VISIBLE
            announcementAdapter.submitList(announcements)
        }
    }

    private fun handleError(error: String?) {
        error?.let {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }
}