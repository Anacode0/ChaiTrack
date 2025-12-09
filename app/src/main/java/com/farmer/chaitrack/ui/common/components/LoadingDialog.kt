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

class LoadingDialog(
    context: Context,
    private val message: String? = null,
    private val cancellable: Boolean = false
) : Dialog(context) {

    private lateinit var progressBar: ProgressBar
    private lateinit var messageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.dialog_loading)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setCancelable(cancellable)
        setCanceledOnTouchOutside(cancellable)

        initializeViews()

        updateMessage(message)
    }

    private fun initializeViews() {
        progressBar = findViewById(R.id.progressBar)
        messageTextView = findViewById(R.id.tvLoadingMessage)
    }

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

    fun show(message: String? = null) {
        if (message != null) {
            updateMessage(message)
        }
        if (!isShowing) {
            super.show()
        }
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
            message: String? = "Loading...",
            cancellable: Boolean = false
        ): LoadingDialog {
            val dialog = LoadingDialog(context, message, cancellable)
            dialog.show()
            return dialog
        }

        fun create(
            context: Context,
            message: String? = "Loading...",
            cancellable: Boolean = false
        ): LoadingDialog {
            return LoadingDialog(context, message, cancellable)
        }
    }
}

fun Context.showLoadingDialog(
    message: String? = "Loading...",
    cancellable: Boolean = false
): LoadingDialog {
    return LoadingDialog.show(this, message, cancellable)
}

class LoadingDialogBuilder(private val context: Context) {
    private var message: String? = "Loading..."
    private var cancellable: Boolean = false
    private var cancelListener: (() -> Unit)? = null

    fun setMessage(message: String?): LoadingDialogBuilder {
        this.message = message
        return this
    }

    fun setCancellable(cancellable: Boolean): LoadingDialogBuilder {
        this.cancellable = cancellable
        return this
    }

    fun setOnCancelListener(listener: () -> Unit): LoadingDialogBuilder {
        this.cancelListener = listener
        return this
    }

    fun build(): LoadingDialog {
        val dialog = LoadingDialog(context, message, cancellable)

        cancelListener?.let { listener ->
            dialog.setOnCancelListener { listener() }
        }

        return dialog
    }

    fun show(): LoadingDialog {
        val dialog = build()
        dialog.show()
        return dialog
    }
}