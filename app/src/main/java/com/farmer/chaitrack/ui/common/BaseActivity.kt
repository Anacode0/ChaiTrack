package com.farmer.chaitrack.ui.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.farmer.chaitrack.R
import com.farmer.chaitrack.ui.auth.LoginActivity
import com.farmer.chaitrack.util.SessionManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Base Activity class that provides common functionality for all activities
 */
@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    /**
     * Flag to check if activity requires authentication
     * Override in child activities to disable authentication check
     */
    protected open val requiresAuthentication: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check authentication if required
        if (requiresAuthentication && !sessionManager.isLoggedIn()) {
            navigateToLogin()
            return
        }
    }

    /**
     * Shows a toast message
     */
    protected fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    /**
     * Shows a long toast message
     */
    protected fun showLongToast(message: String) {
        showToast(message, Toast.LENGTH_LONG)
    }

    /**
     * Shows a snackbar message
     */
    protected fun showSnackbar(
        view: View,
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        actionText: String? = null,
        actionListener: (() -> Unit)? = null
    ) {
        val snackbar = Snackbar.make(view, message, duration)

        if (actionText != null && actionListener != null) {
            snackbar.setAction(actionText) { actionListener() }
        }

        snackbar.show()
    }

    /**
     * Shows an error snackbar with retry action
     */
    protected fun showErrorSnackbar(
        view: View,
        message: String,
        onRetry: (() -> Unit)? = null
    ) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)

        if (onRetry != null) {
            snackbar.setAction("RETRY") { onRetry() }
        }

        snackbar.show()
    }

    /**
     * Shows a confirmation dialog
     */
    protected fun showConfirmationDialog(
        title: String,
        message: String,
        positiveButtonText: String = "Yes",
        negativeButtonText: String = "No",
        onConfirm: () -> Unit,
        onCancel: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            .setNegativeButton(negativeButtonText) { dialog, _ ->
                onCancel?.invoke()
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    /**
     * Shows an alert dialog with single OK button
     */
    protected fun showAlertDialog(
        title: String,
        message: String,
        buttonText: String = "OK",
        onDismiss: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonText) { dialog, _ ->
                onDismiss?.invoke()
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    /**
     * Shows a loading dialog
     */
    private var loadingDialog: AlertDialog? = null

    protected fun showLoadingDialog(message: String = "Please wait...") {
        dismissLoadingDialog()

        loadingDialog = AlertDialog.Builder(this)
            .setView(R.layout.dialog_loading)
            .setMessage(message)
            .setCancelable(false)
            .create()

        loadingDialog?.show()
    }

    /**
     * Dismisses the loading dialog
     */
    protected fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    /**
     * Hides the keyboard
     */
    protected fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    /**
     * Shows the keyboard for a specific view
     */
    protected fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * Navigates to login screen and clears back stack
     */
    protected fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    /**
     * Handles logout with confirmation
     */
    protected fun handleLogout() {
        showConfirmationDialog(
            title = "Logout",
            message = "Are you sure you want to logout?",
            positiveButtonText = "Logout",
            negativeButtonText = "Cancel",
            onConfirm = {
                performLogout()
            }
        )
    }

    /**
     * Performs logout operation
     */
    private fun performLogout() {
        sessionManager.clearSession()
        navigateToLogin()
    }

    /**
     * Checks network connectivity
     */
    protected fun isNetworkAvailable(): Boolean {
        // Implementation would check actual network connectivity
        // For now, returning true
        return true
    }

    /**
     * Shows network error message
     */
    protected fun showNetworkError(view: View) {
        showErrorSnackbar(
            view = view,
            message = "No internet connection. Please check your network settings."
        )
    }

    /**
     * Handles session expiration
     */
    protected fun handleSessionExpired() {
        showAlertDialog(
            title = "Session Expired",
            message = "Your session has expired. Please login again.",
            buttonText = "Login",
            onDismiss = {
                navigateToLogin()
            }
        )
    }

    /**
     * Sets up back button in toolbar
     */
    protected fun setupBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoadingDialog()
    }
}

/**
 * Base Activity for activities that don't require authentication
 */
abstract class BaseNoAuthActivity : BaseActivity() {
    override val requiresAuthentication: Boolean = false
}