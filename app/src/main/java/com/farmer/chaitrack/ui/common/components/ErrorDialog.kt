package com.farmer.chaitrack.ui.common.components

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.farmer.chaitrack.R
import com.farmer.chaitrack.util.Result

/**
 * Reusable error dialog component
 * Shows error messages with an icon and action buttons
 */
class ErrorDialog(
    context: Context,
    private val title: String? = null,
    private val message: String,
    private val errorType: ErrorType = ErrorType.GENERAL,
    private val positiveButtonText: String = "OK",
    private val negativeButtonText: String? = null,
    private val onPositiveClick: (() -> Unit)? = null,
    private val onNegativeClick: (() -> Unit)? = null,
    private val cancellable: Boolean = true
) : Dialog(context) {

    private lateinit var iconImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var messageTextView: TextView
    private lateinit var positiveButton: Button
    private lateinit var negativeButton: Button

    /**
     * Error types with corresponding icons
     */
    enum class ErrorType {
        GENERAL,        // General error
        NETWORK,        // Network/connectivity error
        SERVER,         // Server/API error
        VALIDATION,     // Input validation error
        AUTH,           // Authentication error
        NOT_FOUND,      // Resource not found
        PERMISSION      // Permission denied
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Set content view
        setContentView(R.layout.dialog_error)

        // Make background transparent
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set cancellable behavior
        setCancelable(cancellable)
        setCanceledOnTouchOutside(cancellable)

        // Initialize views
        initializeViews()

        // Setup UI
        setupUI()
    }

    /**
     * Initialize dialog views
     */
    private fun initializeViews() {
        iconImageView = findViewById(R.id.ivErrorIcon)
        titleTextView = findViewById(R.id.tvErrorTitle)
        messageTextView = findViewById(R.id.tvErrorMessage)
        positiveButton = findViewById(R.id.btnPositive)
        negativeButton = findViewById(R.id.btnNegative)
    }

    /**
     * Setup UI elements
     */
    private fun setupUI() {
        // Set icon based on error type
        setErrorIcon()

        // Set title
        if (title.isNullOrBlank()) {
            titleTextView.visibility = android.view.View.GONE
        } else {
            titleTextView.visibility = android.view.View.VISIBLE
            titleTextView.text = title
        }

        // Set message
        messageTextView.text = message

        // Setup positive button
        positiveButton.text = positiveButtonText
        positiveButton.setOnClickListener {
            onPositiveClick?.invoke()
            dismiss()
        }

        // Setup negative button
        if (negativeButtonText.isNullOrBlank()) {
            negativeButton.visibility = android.view.View.GONE
        } else {
            negativeButton.visibility = android.view.View.VISIBLE
            negativeButton.text = negativeButtonText
            negativeButton.setOnClickListener {
                onNegativeClick?.invoke()
                dismiss()
            }
        }
    }

    /**
     * Set error icon based on error type
     */
    private fun setErrorIcon() {
        val iconRes = when (errorType) {
            ErrorType.NETWORK -> R.drawable.ic_no_network
            ErrorType.SERVER -> R.drawable.ic_server_error
            ErrorType.VALIDATION -> R.drawable.ic_warning
            ErrorType.AUTH -> R.drawable.ic_lock_error
            ErrorType.NOT_FOUND -> R.drawable.ic_not_found
            ErrorType.PERMISSION -> R.drawable.ic_permission_denied
            ErrorType.GENERAL -> R.drawable.ic_error
        }
        iconImageView.setImageResource(iconRes)
    }

    /**
     * Safely dismiss the dialog
     */
    fun dismissSafely() {
        try {
            if (isShowing) {
                dismiss()
            }
        } catch (e: Exception) {
            // Dialog might already be dismissed or context destroyed
        }
    }

    companion object {
        /**
         * Create and show an error dialog
         */
        fun show(
            context: Context,
            title: String? = "Error",
            message: String,
            errorType: ErrorType = ErrorType.GENERAL,
            positiveButtonText: String = "OK",
            negativeButtonText: String? = null,
            onPositiveClick: (() -> Unit)? = null,
            onNegativeClick: (() -> Unit)? = null,
            cancellable: Boolean = true
        ): ErrorDialog {
            val dialog = ErrorDialog(
                context,
                title,
                message,
                errorType,
                positiveButtonText,
                negativeButtonText,
                onPositiveClick,
                onNegativeClick,
                cancellable
            )
            dialog.show()
            return dialog
        }

        /**
         * Show error from Result.Error
         */
        fun showFromResult(
            context: Context,
            error: Result.Error,
            onDismiss: (() -> Unit)? = null
        ): ErrorDialog {
            val errorType = when (error.code) {
                Result.ErrorCode.NETWORK_ERROR -> ErrorType.NETWORK
                Result.ErrorCode.SERVER_ERROR -> ErrorType.SERVER
                Result.ErrorCode.VALIDATION_ERROR -> ErrorType.VALIDATION
                Result.ErrorCode.AUTHENTICATION_ERROR,
                Result.ErrorCode.AUTHORIZATION_ERROR -> ErrorType.AUTH
                Result.ErrorCode.NOT_FOUND -> ErrorType.NOT_FOUND
                else -> ErrorType.GENERAL
            }

            val title = when (error.code) {
                Result.ErrorCode.NETWORK_ERROR -> "Connection Error"
                Result.ErrorCode.SERVER_ERROR -> "Server Error"
                Result.ErrorCode.VALIDATION_ERROR -> "Validation Error"
                Result.ErrorCode.AUTHENTICATION_ERROR -> "Authentication Error"
                Result.ErrorCode.AUTHORIZATION_ERROR -> "Access Denied"
                Result.ErrorCode.NOT_FOUND -> "Not Found"
                Result.ErrorCode.TIMEOUT -> "Request Timeout"
                else -> "Error"
            }

            return show(
                context = context,
                title = title,
                message = error.message,
                errorType = errorType,
                onPositiveClick = onDismiss
            )
        }

        /**
         * Show network error with retry option
         */
        fun showNetworkError(
            context: Context,
            message: String = "Please check your internet connection and try again.",
            onRetry: (() -> Unit)? = null
        ): ErrorDialog {
            return show(
                context = context,
                title = "No Internet Connection",
                message = message,
                errorType = ErrorType.NETWORK,
                positiveButtonText = if (onRetry != null) "Retry" else "OK",
                negativeButtonText = if (onRetry != null) "Cancel" else null,
                onPositiveClick = onRetry
            )
        }

        /**
         * Show validation error
         */
        fun showValidationError(
            context: Context,
            message: String
        ): ErrorDialog {
            return show(
                context = context,
                title = "Invalid Input",
                message = message,
                errorType = ErrorType.VALIDATION
            )
        }

        /**
         * Show server error
         */
        fun showServerError(
            context: Context,
            message: String = "Something went wrong on our end. Please try again later.",
            onRetry: (() -> Unit)? = null
        ): ErrorDialog {
            return show(
                context = context,
                title = "Server Error",
                message = message,
                errorType = ErrorType.SERVER,
                positiveButtonText = if (onRetry != null) "Retry" else "OK",
                negativeButtonText = if (onRetry != null) "Cancel" else null,
                onPositiveClick = onRetry
            )
        }

        /**
         * Show authentication error
         */
        fun showAuthError(
            context: Context,
            message: String = "Your session has expired. Please login again.",
            onLogin: (() -> Unit)? = null
        ): ErrorDialog {
            return show(
                context = context,
                title = "Authentication Required",
                message = message,
                errorType = ErrorType.AUTH,
                positiveButtonText = "Login",
                negativeButtonText = "Cancel",
                onPositiveClick = onLogin,
                cancellable = false
            )
        }
    }
}

/**
 * Extension function to show error dialog on Context
 */
fun Context.showErrorDialog(
    title: String? = "Error",
    message: String,
    errorType: ErrorDialog.ErrorType = ErrorDialog.ErrorType.GENERAL,
    onDismiss: (() -> Unit)? = null
): ErrorDialog {
    return ErrorDialog.show(
        context = this,
        title = title,
        message = message,
        errorType = errorType,
        onPositiveClick = onDismiss
    )
}

/**
 * Extension function to show error from Result
 */
fun Context.showError(error: Result.Error, onDismiss: (() -> Unit)? = null): ErrorDialog {
    return ErrorDialog.showFromResult(this, error, onDismiss)
}

/**
 * Builder class for creating error dialogs with more options
 */
class ErrorDialogBuilder(private val context: Context) {
    private var title: String? = "Error"
    private var message: String = ""
    private var errorType: ErrorDialog.ErrorType = ErrorDialog.ErrorType.GENERAL
    private var positiveButtonText: String = "OK"
    private var negativeButtonText: String? = null
    private var onPositiveClick: (() -> Unit)? = null
    private var onNegativeClick: (() -> Unit)? = null
    private var cancellable: Boolean = true

    fun setTitle(title: String?): ErrorDialogBuilder {
        this.title = title
        return this
    }

    fun setMessage(message: String): ErrorDialogBuilder {
        this.message = message
        return this
    }

    fun setErrorType(errorType: ErrorDialog.ErrorType): ErrorDialogBuilder {
        this.errorType = errorType
        return this
    }

    fun setPositiveButton(text: String, onClick: (() -> Unit)? = null): ErrorDialogBuilder {
        this.positiveButtonText = text
        this.onPositiveClick = onClick
        return this
    }

    fun setNegativeButton(text: String, onClick: (() -> Unit)? = null): ErrorDialogBuilder {
        this.negativeButtonText = text
        this.onNegativeClick = onClick
        return this
    }

    fun setCancellable(cancellable: Boolean): ErrorDialogBuilder {
        this.cancellable = cancellable
        return this
    }

    fun build(): ErrorDialog {
        return ErrorDialog(
            context,
            title,
            message,
            errorType,
            positiveButtonText,
            negativeButtonText,
            onPositiveClick,
            onNegativeClick,
            cancellable
        )
    }

    fun show(): ErrorDialog {
        val dialog = build()
        dialog.show()
        return dialog
    }
}