package com.farmer.chaitrack.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
//import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.farmer.chaitrack.R
import com.farmer.chaitrack.databinding.ActivityLoginBinding
import com.farmer.chaitrack.ui.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeState()
    }

    private fun setupUI() {
        binding.etPhoneNumber.doAfterTextChanged { text ->
            val filtered = text?.toString()?.filter { it.isDigit() }
            if (filtered != text?.toString()) {
                binding.etPhoneNumber.setText(filtered)
                binding.etPhoneNumber.setSelection(filtered?.length ?: 0)
            }
            binding.etPhoneNumber.error = null
        }

        binding.etPin.doAfterTextChanged { text ->
            val filtered = text?.toString()?.filter { it.isDigit() }
            if (filtered != text?.toString()) {
                binding.etPin.setText(filtered)
                binding.etPin.setSelection(filtered?.length ?: 0)
            }

            filtered?.let {
                if (it.length < 5 || it.length > 10) {
                    binding.etPin.error = getString(R.string.error_pin_length)
                } else {
                    binding.etPin.error = null
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        binding.tvForgotPin.setOnClickListener {
            handleForgotPin()
        }
    }

    private fun handleLogin() {
        val phoneNumber = binding.etPhoneNumber.text?.toString()?.trim() ?: ""
        val pin = binding.etPin.text?.toString()?.trim() ?: ""
        var isValid = true

        if (phoneNumber.isEmpty()) {
            binding.etPhoneNumber.error = getString(R.string.error_phone_required)
            isValid = false
        } else if (phoneNumber.length < 10) {
            binding.etPhoneNumber.error = getString(R.string.error_phone_invalid)
            isValid = false
        }

        if (pin.isEmpty()) {
            binding.etPin.error = getString(R.string.error_pin_required)
            isValid = false
        } else if (pin.length < 5 || pin.length > 10) {
            binding.etPin.error = getString(R.string.error_pin_length)
            isValid = false
        }

        if (isValid) {
            viewModel.login(phoneNumber, pin)
        }
    }

    private fun handleForgotPin() {
        val phoneNumber = binding.etPhoneNumber.text?.toString()?.trim() ?: ""

        if (phoneNumber.isEmpty()) {
            binding.etPhoneNumber.error = getString(R.string.error_phone_required)
            return
        } else if (phoneNumber.length < 10) {
            binding.etPhoneNumber.error = getString(R.string.error_phone_invalid)
            return
        }

        viewModel.recoverPin(phoneNumber)
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    handleState(state)
                }
            }
        }
    }

    private fun handleState(state: LoginState) {

        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !state.isLoading
        binding.tvForgotPin.isEnabled = !state.isLoading


        if (state.isLoginSuccessful) {
            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
            navigateToMain()
        }

        if (state.isPinRecoverySent) {
            Toast.makeText(this, getString(R.string.pin_recovery_sent), Toast.LENGTH_LONG).show()
            viewModel.resetPinRecoveryState()
        }

        state.error?.let { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}