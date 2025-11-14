package com.farmer.chaitrack.ui.finance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.farmer.chaitrack.R
import com.farmer.chaitrack.databinding.ActivityFinanceBinding
import com.farmer.chaitrack.databinding.DialogReportIssueBinding
import kotlinx.coroutines.launch
import java.io.File
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class FinanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinanceBinding
    private val viewModel: FinanceViewModel by viewModels()

    private val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDropdowns()
        setupButtons()
        observeState()

        // Set current month and year as default
        setDefaultMonthYear()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupDropdowns() {
        // Setup Month Dropdown
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, months)
        binding.actvMonth.setAdapter(monthAdapter)
        binding.actvMonth.setOnItemClickListener { _, _, position, _ ->
            viewModel.setMonth(position + 1)
            viewModel.loadTranscriptData()
        }

        // Setup Year Dropdown
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val firstYear = viewModel.getFirstLoginYear()
        val years = (firstYear..currentYear).toList().reversed().map { it.toString() }

        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, years)
        binding.actvYear.setAdapter(yearAdapter)
        binding.actvYear.setOnItemClickListener { _, _, position, _ ->
            val selectedYear = years[position].toInt()
            viewModel.setYear(selectedYear)
            viewModel.loadTranscriptData()
        }
    }

    private fun setDefaultMonthYear() {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        binding.actvMonth.setText(months[currentMonth], false)
        binding.actvYear.setText(currentYear.toString(), false)

        viewModel.setMonth(currentMonth + 1)
        viewModel.setYear(currentYear)
        viewModel.loadTranscriptData()
    }

    private fun setupButtons() {
        binding.btnGeneratePdf.setOnClickListener {
            viewModel.generatePdf()
        }

        binding.btnShareWithBank.setOnClickListener {
            viewModel.shareWithBank()
        }

        binding.fabReportIssue.setOnClickListener {
            showReportIssueDialog()
        }
    }

    private fun showReportIssueDialog() {
        val dialogBinding = DialogReportIssueBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setTitle(getString(R.string.report_issue))
            .create()

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnSubmit.setOnClickListener {
            val issueDescription = dialogBinding.etIssueDescription.text?.toString()?.trim()

            if (issueDescription.isNullOrEmpty()) {
                dialogBinding.etIssueDescription.error = getString(R.string.issue_description_empty)
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

    private fun updateUI(state: FinanceState) {
        // Show/Hide loading
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

        // Update preview card
        if (state.selectedMonth != null && state.selectedYear != null) {
            val monthName = months[state.selectedMonth - 1]
            binding.tvPreviewTitle.text = getString(
                R.string.transcript_for_month_year,
                monthName,
                state.selectedYear
            )
        }

        // Update weight
        binding.tvTotalWeight.text = formatWeight(state.totalWeight)

        // Update price per kg
        binding.tvPricePerKg.text = getString(R.string.price_per_kg_format, state.pricePerKg)

        // Update total earnings
        binding.tvTotalEarnings.text = getString(R.string.total_earnings_format, state.totalEarnings)

        // Enable/Disable buttons based on data availability
        val hasData = state.totalWeight > 0
        binding.btnGeneratePdf.isEnabled = hasData && !state.isLoading
        binding.btnShareWithBank.isEnabled = hasData && !state.isLoading && state.pdfFile != null

        // Handle PDF generation success
        if (state.pdfGenerated && state.pdfFile != null) {
            Toast.makeText(this, getString(R.string.pdf_generated_success), Toast.LENGTH_SHORT).show()
            binding.btnShareWithBank.isEnabled = true
        }

        // Handle share action
        if (state.shouldShare && state.pdfFile != null) {
            sharePdfWithBank(state.pdfFile)
            viewModel.resetShareState()
        }

        // Handle issue report success
        if (state.issueReported) {
            Toast.makeText(this, getString(R.string.issue_submitted_success), Toast.LENGTH_SHORT).show()
            viewModel.resetIssueReportedState()
        }

        // Handle errors
        state.error?.let { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }

        // Show message if no data available
        if (!state.isLoading && state.totalWeight == 0.0) {
            Toast.makeText(this, getString(R.string.no_data_available), Toast.LENGTH_SHORT).show()
        }
    }

    private fun sharePdfWithBank(pdfFile: File) {
        try {
            val uri = FileProvider.getUriForFile(
                this,
                "${packageName}.provider",
                pdfFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.salary_transcripts))
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.share_pdf_message)
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.pdf_share_error, e.message), Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatWeight(weightInKg: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 1
        return getString(R.string.weight_format_kg, formatter.format(weightInKg))
    }
}