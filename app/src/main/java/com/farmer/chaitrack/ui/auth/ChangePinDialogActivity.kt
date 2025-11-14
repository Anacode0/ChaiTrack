package com.farmer.chaitrack.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.farmer.chaitrack.R
import com.farmer.chaitrack.databinding.DialogChangePinBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ChangePinDialogActivity : AppCompatActivity() {

    private lateinit var binding: DialogChangePinBinding
    private val viewModel: ChangePinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDialogWindow()

        binding = DialogChangePinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackPressHandler()
        setupListeners()
        observeState()
    }

    private fun setupDialogWindow() {
        window?.let { window ->
            val displayMetrics = resources.displayMetrics
            val width = (displayMetrics.widthPixels * 0.90).toInt()
            val maxHeight = (displayMetrics.heightPixels * 0.70).toInt()

            window.setLayout(width, maxHeight)

            val params = window.attributes
            params.flags = params.flags or android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND
            window.attributes = params

            window.setGravity(android.view.Gravity.CENTER)
            window.setWindowAnimations(android.R.style.Animation_Dialog)
        }
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

    private fun setupListeners() {
        // Clear errors when user starts typing
        binding.etCurrentPin.doAfterTextChanged {
            viewModel.clearError()
            binding.tilCurrentPin.error = null
        }

        binding.etNewPin.doAfterTextChanged {
            viewModel.clearError()
            binding.tilNewPin.error = null
        }

        binding.etConfirmPin.doAfterTextChanged {
            viewModel.clearError()
            binding.tilConfirmPin.error = null
        }

        // Cancel button
        binding.btnCancel.setOnClickListener {
            finishWithAnimation()
        }

        // Change PIN button
        binding.btnChange.setOnClickListener {
            val currentPin = binding.etCurrentPin.text?.toString() ?: ""
            val newPin = binding.etNewPin.text?.toString() ?: ""
            val confirmPin = binding.etConfirmPin.text?.toString() ?: ""

            viewModel.changePin(currentPin, newPin, confirmPin)
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

    private fun updateUI(state: ChangePinState) {
        // Show/hide loading
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

        // Disable inputs during loading
        binding.etCurrentPin.isEnabled = !state.isLoading
        binding.etNewPin.isEnabled = !state.isLoading
        binding.etConfirmPin.isEnabled = !state.isLoading
        binding.btnChange.isEnabled = !state.isLoading
        binding.btnCancel.isEnabled = !state.isLoading

        // Show error
        if (state.error != null) {
            binding.errorTextView.text = state.error
            binding.errorTextView.visibility = View.VISIBLE
            binding.successTextView.visibility = View.GONE

            // Set error on appropriate field
            when {
                state.error.contains("current PIN", ignoreCase = true) -> {
                    binding.tilCurrentPin.error = state.error
                }
                state.error.contains("new PIN", ignoreCase = true) ||
                        state.error.contains("must be", ignoreCase = true) -> {
                    binding.tilNewPin.error = state.error
                }
                state.error.contains("match", ignoreCase = true) -> {
                    binding.tilConfirmPin.error = state.error
                }
            }
        } else {
            binding.errorTextView.visibility = View.GONE
            binding.tilCurrentPin.error = null
            binding.tilNewPin.error = null
            binding.tilConfirmPin.error = null
        }

        // Show success
        if (state.isSuccess) {
            binding.successTextView.visibility = View.VISIBLE
            binding.errorTextView.visibility = View.GONE

            // Show success dialog and finish
            showSuccessAndFinish()
        } else {
            binding.successTextView.visibility = View.GONE
        }
    }

    private fun showSuccessAndFinish() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Success")
            .setMessage("Your PIN has been changed successfully")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                setResult(RESULT_OK)
                finishWithAnimation()
            }
            .setCancelable(false)
            .show()
    }

    companion object {
        const val REQUEST_CODE_CHANGE_PIN = 1001
    }
}