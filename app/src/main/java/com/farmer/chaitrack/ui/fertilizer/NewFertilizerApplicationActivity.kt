package com.farmer.chaitrack.ui.fertilizer

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.farmer.chaitrack.R
import com.farmer.chaitrack.databinding.ActivityNewFertilizerApplicationBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class NewFertilizerApplicationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewFertilizerApplicationBinding
    private val viewModel: NewApplicationViewModel by viewModels()
    private var isSubmitting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDialogWindow()

        binding = ActivityNewFertilizerApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupBackPressHandler()
        setupSeasonDropdown()
        setupPlotSize()
        setupQuantitySlider()
        setupSubmitButton()
        observeState()

        viewModel.loadFarmerData()
    }

    private fun setupDialogWindow() {
        window?.let { window ->
            val displayMetrics = resources.displayMetrics
            val width = android.view.WindowManager.LayoutParams.MATCH_PARENT
            val height = android.view.WindowManager.LayoutParams.WRAP_CONTENT

            window.setLayout(width, height)

            window.setGravity(android.view.Gravity.CENTER)

            window.setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_close)
        binding.toolbar.setNavigationOnClickListener {
            finishWithAnimation()
        }

        binding.toolbar.title = getString(R.string.new_fertilizer_application)
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishWithAnimation()
            }
        })
    }

    private fun finishWithAnimation() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun setupSeasonDropdown() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val seasons = listOf(
            getString(R.string.season, currentYear + 1),
            getString(R.string.fall, currentYear)
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, seasons)
        binding.actvSeason.setAdapter(adapter)
        binding.actvSeason.setOnItemClickListener { _, _, position, _ ->
            viewModel.setSeason(seasons.getOrNull(position) ?: "")
        }

        if (binding.actvSeason.text.isNullOrEmpty() && seasons.isNotEmpty()) {
            binding.actvSeason.setText(seasons[0], false)
            viewModel.setSeason(seasons[0])
        }
    }

    private fun setupPlotSize() {
        binding.etPlotSize.isFocusable = false
        binding.etPlotSize.isClickable = false
    }

    private fun setupQuantitySlider() {
        binding.sliderQuantity.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                viewModel.setQuantity(value.toInt())
            }
        }
    }

    private fun setupSubmitButton() {
        binding.btnSubmitApplication.setOnClickListener {
            if (!isSubmitting && validateForm()) {
                isSubmitting = true
                binding.btnSubmitApplication.isEnabled = false
                viewModel.submitApplication()
            }
        }
    }

    private fun validateForm(): Boolean {
        val season = binding.actvSeason.text?.toString()

        if (season.isNullOrEmpty()) {
            binding.tilSeason.error = getString(R.string.select_season_error)
            return false
        }

        binding.tilSeason.error = null
        return true
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

    private fun updateUI(state: NewApplicationState) {
        runOnUiThread {
            binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

            if (!state.isLoading && !state.applicationSubmitted) {
                isSubmitting = false
                binding.btnSubmitApplication.isEnabled = true
            }

            val plotSizeText = state.plotSize?.toString() ?: "0.0"
            if (binding.etPlotSize.text?.toString() != plotSizeText) {
                binding.etPlotSize.setText(plotSizeText)
            }

            val plotSize = state.plotSize ?: 0.0
            val recommended = calculateRecommendedBags(plotSize)
            binding.tvRecommendedAmount.text = getString(R.string.recommended_amount, recommended)

            val maxValue = (recommended * 2).toFloat()
            if (binding.sliderQuantity.valueTo != maxValue) {
                binding.sliderQuantity.valueTo = maxValue
            }

            val currentQuantity = if (state.quantityNeeded > 0) state.quantityNeeded else recommended
            if (binding.sliderQuantity.value != currentQuantity.toFloat()) {
                binding.sliderQuantity.value = currentQuantity.toFloat()
            }

            binding.tvQuantityValue.text = getString(R.string.bags_format, currentQuantity)

            if (state.applicationSubmitted && !isSubmitting) {
                Toast.makeText(
                    this@NewFertilizerApplicationActivity,
                    getString(R.string.application_submitted),
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_OK)
                finishWithAnimation()
            }

            state.error?.let { error ->
                Toast.makeText(this@NewFertilizerApplicationActivity, error, Toast.LENGTH_LONG).show()
                viewModel.clearError()
                isSubmitting = false
                binding.btnSubmitApplication.isEnabled = true
            }
        }
    }

    private fun calculateRecommendedBags(plotSize: Double): Int {
        return (plotSize * 10).toInt().coerceAtLeast(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.sliderQuantity.clearOnChangeListeners()
    }
}