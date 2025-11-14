package com.farmer.chaitrack.ui.common.components

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import com.farmer.chaitrack.R

/**
 * Reusable loading dialog component
 * Shows a progress indicator with an optional message
 * Cannot be cancelled by tapping outside or back button by default
 */
class LoadingDialog(
    context: Context,
    private val message: String? = null,
    private val cancellable: Boolean = false
) : Dialog(context) {

    private lateinit var progressBar: ProgressBar
    private lateinit var messageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Set content view
        setContentView(R.layout.dialog_loading)

        // Make background transparent
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set cancellable behavior
        setCancelable(cancellable)
        setCanceledOnTouchOutside(cancellable)

        // Initialize views
        initializeViews()

        // Set message if provided
        updateMessage(message)
    }

    /**
     * Initialize dialog views
     */
    private fun initializeViews() {
        progressBar = findViewById(R.id.progressBar)
        messageTextView = findViewById(R.id.tvLoadingMessage)
    }

    /**
     * Update the loading message
     */
    fun updateMessage(newMessage: String?) {
        if (::messageTextView.isInitialized) {
            if (newMessage.isNullOrBlank()) {
                messageTextView.visibility = android.view.View.GONE
            } else {
                messageTextView.visibility = android.view.View.VISIBLE
                messageTextView.text = newMessage
            }
        }
    }

    /**
     * Show the dialog with optional message
     */
    fun show(message: String? = null) {
        if (message != null) {
            updateMessage(message)
        }
        if (!isShowing) {
            super.show()
        }
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
         * Create and show a loading dialog
         */
        fun show(
            context: Context,
            message: String? = "Loading...",
            cancellable: Boolean = false
        ): LoadingDialog {
            val dialog = LoadingDialog(context, message, cancellable)
            dialog.show()
            return dialog
        }

        /**
         * Create a loading dialog without showing it
         */
        fun create(
            context: Context,
            message: String? = "Loading...",
            cancellable: Boolean = false
        ): LoadingDialog {
            return LoadingDialog(context, message, cancellable)
        }
    }
}

/**
 * Extension function to show loading dialog on Context
 */
fun Context.showLoadingDialog(
    message: String? = "Loading...",
    cancellable: Boolean = false
): LoadingDialog {
    return LoadingDialog.show(this, message, cancellable)
}

/**
 * Builder class for creating loading dialogs with more options
 */
class LoadingDialogBuilder(private val context: Context) {
    private var message: String? = "Loading..."
    private var cancellable: Boolean = false
    private var cancelListener: (() -> Unit)? = null

    /**
     * Set the loading message
     */
    fun setMessage(message: String?): LoadingDialogBuilder {
        this.message = message
        return this
    }

    /**
     * Set whether the dialog is cancellable
     */
    fun setCancellable(cancellable: Boolean): LoadingDialogBuilder {
        this.cancellable = cancellable
        return this
    }

    /**
     * Set a listener for when dialog is cancelled
     */
    fun setOnCancelListener(listener: () -> Unit): LoadingDialogBuilder {
        this.cancelListener = listener
        return this
    }

    /**
     * Build the dialog
     */
    fun build(): LoadingDialog {
        val dialog = LoadingDialog(context, message, cancellable)

        cancelListener?.let { listener ->
            dialog.setOnCancelListener { listener() }
        }

        return dialog
    }

    /**
     * Build and show the dialog
     */
    fun show(): LoadingDialog {
        val dialog = build()
        dialog.show()
        return dialog
    }
}