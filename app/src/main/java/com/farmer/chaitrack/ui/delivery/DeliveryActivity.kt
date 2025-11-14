package com.farmer.chaitrack.ui.delivery

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmer.chaitrack.R
import com.farmer.chaitrack.databinding.ActivityDeliveryBinding
import com.farmer.chaitrack.databinding.DialogReportIssueBinding
import com.farmer.chaitrack.ui.delivery.adapter.DeliveryAdapter
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import java.util.Calendar

class DeliveryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityDeliveryBinding
    private val viewModel: DeliveryViewModel by viewModels()
    private lateinit var deliveryAdapter: DeliveryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupNavigationDrawer()
        setupRecyclerView()
        setupFAB()
        setupBackPressedHandler()
        observeState()

        // Load initial data
        viewModel.loadDeliveries(DeliveryFilter.THIS_MONTH)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setupNavigationDrawer() {
        binding.navigationView.setNavigationItemSelectedListener(this)

        // Add years dynamically
        addYearsToMenu()

        // Set default selection
        binding.navigationView.setCheckedItem(R.id.nav_this_month)
    }

    private fun addYearsToMenu() {
        val menu = binding.navigationView.menu
        // Find the "By Year" menu item (last item in the menu)
        val byYearMenuItem = menu.getItem(menu.size() - 1)
        val yearGroup = byYearMenuItem.subMenu

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val firstYear = viewModel.getFirstLoginYear() // TODO: Get from repository

        for (year in currentYear downTo firstYear) {
            if (year != currentYear) {
                yearGroup?.add(android.view.Menu.NONE, android.view.Menu.NONE, year, year.toString())
                    ?.setCheckable(true)
            }
        }
    }

    private fun setupRecyclerView() {
        deliveryAdapter = DeliveryAdapter()
        binding.rvDeliveries.apply {
            this.layoutManager = LinearLayoutManager(this@DeliveryActivity)
            this.adapter = deliveryAdapter
        }
    }

    private fun setupFAB() {
        binding.fabReportIssue.setOnClickListener {
            showReportIssueDialog()
        }
    }

    private fun setupBackPressedHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    finish()
                }
            }
        })
    }

    private fun showReportIssueDialog() {
        val dialogBinding = DialogReportIssueBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnSubmit.setOnClickListener {
            val issueDescription = dialogBinding.etIssueDescription.text?.toString()?.trim()

            if (issueDescription.isNullOrEmpty()) {
                Toast.makeText(this, getString(R.string.issue_description_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.reportIssue(issueDescription)
            dialog.dismiss()
        }

        dialog.show()
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

    private fun updateUI(state: DeliveryState) {
        // Show/Hide loading
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

        // Update deliveries list
        if (state.deliveries.isEmpty() && !state.isLoading) {
            binding.textEmptyState.visibility = View.VISIBLE
            binding.rvDeliveries.visibility = View.GONE
        } else {
            binding.textEmptyState.visibility = View.GONE
            binding.rvDeliveries.visibility = View.VISIBLE
            deliveryAdapter.submitList(state.deliveries)
        }

        // Handle issue report success
        if (state.issueReported) {
            Toast.makeText(this, getString(R.string.issue_submitted_success), Toast.LENGTH_SHORT).show()
            viewModel.resetIssueReportedState()
        }

        // Handle error
        state.error?.let { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_all_deliveries -> {
                viewModel.loadDeliveries(DeliveryFilter.ALL)
            }
            R.id.nav_this_month -> {
                viewModel.loadDeliveries(DeliveryFilter.THIS_MONTH)
            }
            R.id.nav_last_two_weeks -> {
                viewModel.loadDeliveries(DeliveryFilter.LAST_TWO_WEEKS)
            }
            R.id.nav_last_month -> {
                viewModel.loadDeliveries(DeliveryFilter.LAST_MONTH)
            }
            R.id.nav_last_three_months -> {
                viewModel.loadDeliveries(DeliveryFilter.LAST_THREE_MONTHS)
            }
            R.id.nav_last_six_months -> {
                viewModel.loadDeliveries(DeliveryFilter.LAST_SIX_MONTHS)
            }
            R.id.nav_last_year -> {
                viewModel.loadDeliveries(DeliveryFilter.LAST_YEAR)
            }
            R.id.nav_current_year -> {
                viewModel.loadDeliveriesByYear(Calendar.getInstance().get(Calendar.YEAR))
            }
            else -> {
                // Handle year selections from dynamically added years
                val year = item.title.toString().toIntOrNull()
                year?.let {
                    viewModel.loadDeliveriesByYear(it)
                }
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}