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

    enum class ErrorType {
        GENERAL,
        NETWORK,
        SERVER,
        VALIDATION,
        AUTH,
        NOT_FOUND,
        PERMISSION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.dialog_error)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setCancelable(cancellable)
        setCanceledOnTouchOutside(cancellable)

        initializeViews()

        setupUI()
    }

    private fun initializeViews() {
        iconImageView = findViewById(R.id.ivErrorIcon)
        titleTextView = findViewById(R.id.tvErrorTitle)
        messageTextView = findViewById(R.id.tvErrorMessage)
        positiveButton = findViewById(R.id.btnPositive)
        negativeButton = findViewById(R.id.btnNegative)
    }

    private fun setupUI() {
        setErrorIcon()

        if (title.isNullOrBlank()) {
            titleTextView.visibility = android.view.View.GONE
        } else {
            titleTextView.visibility = android.view.View.VISIBLE
            titleTextView.text = title
        }

        messageTextView.text = message

        positiveButton.text = positiveButtonText
        positiveButton.setOnClickListener {
            onPositiveClick?.invoke()
            dismiss()
        }

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

    fun dismissSafely() {
        try {
            if (isShowing) {
                dismiss()
            }
        } catch (e: Exception) {
        }
    }

    companion object {
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

fun Context.showError(error: Result.Error, onDismiss: (() -> Unit)? = null): ErrorDialog {
    return ErrorDialog.showFromResult(this, error, onDismiss)
}

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