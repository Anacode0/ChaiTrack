package com.farmer.chaitrack.ui.fertilizer

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farmer.chaitrack.R
import com.farmer.chaitrack.domain.model.FertilizerApplication
import com.farmer.chaitrack.ui.fertilizer.adapter.FertilizerApplicationAdapter
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.progressindicator.LinearProgressIndicator


class FertilizerActivity : AppCompatActivity() {

    private lateinit var rvApplicationHistory: RecyclerView
    private lateinit var progressBar: android.widget.ProgressBar
    private lateinit var btnApplyNextSeason: android.widget.Button
    private lateinit var tvSeason: TextView
    private lateinit var tvStatusLabel: TextView
    private lateinit var tvQuantityRequested: TextView
    private lateinit var tvQuantityApproved: TextView
    private lateinit var tvQuantityApprovedLabel: TextView
    private lateinit var progressStatus: LinearProgressIndicator
    private lateinit var vEmptyHistory: View
    private lateinit var adapter: FertilizerApplicationAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fertilizer)

        initializeViews()
        setupRecyclerView()
        setupToolbar()
        setupClickListeners()
        loadData()
    }

    private fun initializeViews() {
        rvApplicationHistory = findViewById(R.id.rvApplicationHistory)
        progressBar = findViewById(R.id.progressBar)
        btnApplyNextSeason = findViewById(R.id.btnApplyNextSeason)
        vEmptyHistory = findViewById(R.id.tvEmptyHistory)
        tvSeason = findViewById(R.id.tvSeason)
        tvStatusLabel = findViewById(R.id.tvStatusLabel)
        tvQuantityRequested = findViewById(R.id.tvQuantityRequested)
        tvQuantityApproved = findViewById(R.id.tvQuantityApproved)
        tvQuantityApprovedLabel = findViewById(R.id.tvQuantityApprovedLabel)
        progressStatus = findViewById(R.id.progressStatus)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun loadApplicationHistory() {
        val historyList = listOf(
            FertilizerApplication(
                id = "APP001",
                season = "May 2025",
                plotSize = 2.5,
                quantityRequested = 5,
                quantityApproved = 5,
                recommendedQuantity = 5,
                status = ApplicationStatus.COMPLETED,
                applicationDate = System.currentTimeMillis() - 86400000L
            ),
            FertilizerApplication(
                id = "APP002",
                season = "May 2024",
                plotSize = 2.5,
                quantityRequested = 5,
                quantityApproved = 5,
                recommendedQuantity = 5,
                status = ApplicationStatus.COMPLETED,
                applicationDate = System.currentTimeMillis() - 31536000000L
            )
        )

        adapter.submitList(historyList)
        toggleEmptyState(historyList.isEmpty())
    }

    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(this@FertilizerActivity)
        rvApplicationHistory.layoutManager = layoutManager
        adapter = FertilizerApplicationAdapter()
        rvApplicationHistory.adapter = adapter
        loadApplicationHistory()
    }

    private val newApplicationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            loadApplicationHistory()
            loadData()
            Toast.makeText(this, "Application submitted successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListeners() {
        //btnApplyNextSeason.setOnClickListener {
            //startNewApplicationActivity()
        //}
        val fabApplyFertilizer = findViewById<ExtendedFloatingActionButton>(R.id.fabApplyFertilizer)
        fabApplyFertilizer.setOnClickListener {
            startNewApplicationActivity()
        }
    }
    private fun startNewApplicationActivity() {
        val intent = Intent(this, NewFertilizerApplicationActivity::class.java)
        newApplicationLauncher.launch(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun loadData() {
        val tvSeason = findViewById<TextView>(R.id.tvSeason)
        val tvStatusLabel = findViewById<TextView>(R.id.tvStatusLabel)
        val tvQuantityRequested = findViewById<TextView>(R.id.tvQuantityRequested)
        val tvQuantityApproved = findViewById<TextView>(R.id.tvQuantityApproved)
        val tvQuantityApprovedLabel = findViewById<TextView>(R.id.tvQuantityApprovedLabel)

        tvSeason.text = "May 2025 Application"
        tvStatusLabel.text = "Pending Review"
        tvQuantityRequested.text = "50 bags"


        val currentStatus = ApplicationStatus.PENDING_REVIEW

        if (currentStatus == ApplicationStatus.APPROVED ||
            currentStatus == ApplicationStatus.READY_FOR_PICKUP ||
            currentStatus == ApplicationStatus.PICKED_UP ||
            currentStatus == ApplicationStatus.COMPLETED) {
            tvQuantityApproved.text = "50 bags"
            tvQuantityApproved.visibility = View.VISIBLE
            tvQuantityApprovedLabel.visibility = View.VISIBLE
        } else {
            tvQuantityApproved.visibility = View.GONE
            tvQuantityApprovedLabel.visibility = View.GONE
        }

        setupProgressIndicator(currentStatus)

        progressBar.visibility = View.GONE

        loadApplicationHistory()
    }

    private fun handleApplicationStatus(status: ApplicationStatus) {
        when (status) {
            ApplicationStatus.PENDING_REVIEW -> handlePendingStatus()
            ApplicationStatus.UNDER_REVIEW -> handleUnderReviewStatus()
            ApplicationStatus.APPROVED -> handleApprovedStatus()
            ApplicationStatus.READY_FOR_PICKUP -> handleReadyForPickupStatus()
            ApplicationStatus.PICKED_UP -> handlePickedUpStatus()
            ApplicationStatus.REJECTED -> handleRejectedStatus()
            ApplicationStatus.COMPLETED -> handleCompletedStatus()
        }
    }

    private fun updateStatusUI(status: ApplicationStatus) {
        if (status == ApplicationStatus.APPROVED) {
            val successColor = ContextCompat.getColor(this, R.color.success_green)
        } else if (status == ApplicationStatus.REJECTED) {
            val errorColor = ContextCompat.getColor(this, R.color.error_red)
        }
    }

    private fun getStatusColor(status: ApplicationStatus): Int {
        return when (status) {
            ApplicationStatus.PENDING_REVIEW -> ContextCompat.getColor(this, android.R.color.darker_gray)
            ApplicationStatus.UNDER_REVIEW -> ContextCompat.getColor(this, R.color.warning_orange)
            ApplicationStatus.APPROVED -> ContextCompat.getColor(this, R.color.success_green)
            ApplicationStatus.READY_FOR_PICKUP -> ContextCompat.getColor(this, R.color.info_blue)
            ApplicationStatus.PICKED_UP -> ContextCompat.getColor(this, R.color.success_green)
            ApplicationStatus.REJECTED -> ContextCompat.getColor(this, R.color.error_red)
            ApplicationStatus.COMPLETED -> ContextCompat.getColor(this, R.color.success_green)
        }
    }

    private fun handlePendingStatus() {
        progressBar.visibility = android.view.View.VISIBLE
    }

    private fun handleUnderReviewStatus() {
        progressBar.visibility = android.view.View.VISIBLE
    }

    private fun handleApprovedStatus() {
        progressBar.visibility = android.view.View.GONE
    }

    private fun handleReadyForPickupStatus() {
        progressBar.visibility = android.view.View.GONE
    }

    private fun handlePickedUpStatus() {
        progressBar.visibility = android.view.View.GONE
    }

    private fun handleRejectedStatus() {
        progressBar.visibility = android.view.View.GONE
    }

    private fun handleCompletedStatus() {
        progressBar.visibility = android.view.View.GONE
    }

    private fun setupProgressIndicator(status: ApplicationStatus) {
        val progressStatus = findViewById<LinearProgressIndicator>(R.id.progressStatus)

        when (status) {
            ApplicationStatus.PENDING_REVIEW -> {
                progressStatus.progress = 25
                progressStatus.setIndicatorColor(ContextCompat.getColor(this, R.color.warning_orange))
            }
            ApplicationStatus.UNDER_REVIEW -> {
                progressStatus.progress = 50
                progressStatus.setIndicatorColor(ContextCompat.getColor(this, R.color.info_blue))
            }
            ApplicationStatus.APPROVED -> {
                progressStatus.progress = 75
                progressStatus.setIndicatorColor(ContextCompat.getColor(this, R.color.success_green))
            }
            ApplicationStatus.COMPLETED -> {
                progressStatus.progress = 100
                progressStatus.setIndicatorColor(ContextCompat.getColor(this, R.color.success_green))
            }
            else -> {
                progressStatus.progress = 0
            }
        }
    }

    private fun toggleEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            vEmptyHistory.visibility = android.view.View.VISIBLE
            rvApplicationHistory.visibility = android.view.View.GONE
        } else {
            vEmptyHistory.visibility = android.view.View.GONE
            rvApplicationHistory.visibility = android.view.View.VISIBLE
        }
    }
}