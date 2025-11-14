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

/**
 * Reusable confirmation dialog component
 * Shows a confirmation prompt with Yes/No or custom action buttons
 */
class ConfirmationDialog(
    context: Context,
    private val title: String,
    private val message: String,
    private val confirmationType: ConfirmationType = ConfirmationType.NORMAL,
    private val positiveButtonText: String = "Confirm",
    private val negativeButtonText: String = "Cancel",
    private val onConfirm: (() -> Unit)? = null,
    private val onCancel: (() -> Unit)? = null,
    private val cancellable: Boolean = true
) : Dialog(context) {

    private lateinit var iconImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var messageTextView: TextView
    private lateinit var positiveButton: Button
    private lateinit var negativeButton: Button

    /**
     * Confirmation types with corresponding icons and button styles
     */
    enum class ConfirmationType {
        NORMAL,         // General confirmation (blue/neutral)
        WARNING,        // Warning action (orange)
        DESTRUCTIVE,    // Destructive action like delete (red)
        SUCCESS,        // Success confirmation (green)
        INFO            // Information confirmation (blue)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Set content view
        setContentView(R.layout.dialog_confirmation)

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
        iconImageView = findViewById(R.id.ivConfirmationIcon)
        titleTextView = findViewById(R.id.tvConfirmationTitle)
        messageTextView = findViewById(R.id.tvConfirmationMessage)
        positiveButton = findViewById(R.id.btnConfirmPositive)
        negativeButton = findViewById(R.id.btnConfirmNegative)
    }

    /**
     * Setup UI elements
     */
    private fun setupUI() {
        // Set icon and styling based on confirmation type
        setConfirmationStyle()

        // Set title
        titleTextView.text = title

        // Set message
        messageTextView.text = message

        // Setup positive button
        positiveButton.text = positiveButtonText
        positiveButton.setOnClickListener {
            onConfirm?.invoke()
            dismiss()
        }

        // Setup negative button
        negativeButton.text = negativeButtonText
        negativeButton.setOnClickListener {
            onCancel?.invoke()
            dismiss()
        }
    }

    /**
     * Set confirmation style based on type
     */
    private fun setConfirmationStyle() {
        when (confirmationType) {
            ConfirmationType.NORMAL -> {
                iconImageView.setImageResource(R.drawable.ic_help)
                // Apply default button style
            }
            ConfirmationType.WARNING -> {
                iconImageView.setImageResource(R.drawable.ic_warning)
                // Apply warning button style (orange)
                positiveButton.setBackgroundResource(R.drawable.bg_button_warning)
            }
            ConfirmationType.DESTRUCTIVE -> {
                iconImageView.setImageResource(R.drawable.ic_delete)
                // Apply destructive button style (red)
                positiveButton.setBackgroundResource(R.drawable.bg_button_destructive)
            }
            ConfirmationType.SUCCESS -> {
                iconImageView.setImageResource(R.drawable.ic_check_circle)
                // Apply success button style (green)
                positiveButton.setBackgroundResource(R.drawable.bg_button_success)
            }
            ConfirmationType.INFO -> {
                iconImageView.setImageResource(R.drawable.ic_info)
                // Apply info button style (blue)
            }
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
         * Create and show a confirmation dialog
         */
        fun show(
            context: Context,
            title: String,
            message: String,
            confirmationType: ConfirmationType = ConfirmationType.NORMAL,
            positiveButtonText: String = "Confirm",
            negativeButtonText: String = "Cancel",
            onConfirm: (() -> Unit)? = null,
            onCancel: (() -> Unit)? = null,
            cancellable: Boolean = true
        ): ConfirmationDialog {
            val dialog = ConfirmationDialog(
                context,
                title,
                message,
                confirmationType,
                positiveButtonText,
                negativeButtonText,
                onConfirm,
                onCancel,
                cancellable
            )
            dialog.show()
            return dialog
        }

        /**
         * Show logout confirmation
         */
        fun showLogoutConfirmation(
            context: Context,
            onConfirm: () -> Unit
        ): ConfirmationDialog {
            return show(
                context = context,
                title = "Logout",
                message = "Are you sure you want to logout?",
                confirmationType = ConfirmationType.WARNING,
                positiveButtonText = "Logout",
                negativeButtonText = "Cancel",
                onConfirm = onConfirm
            )
        }

        /**
         * Show delete confirmation
         */
        fun showDeleteConfirmation(
            context: Context,
            itemName: String = "this item",
            onConfirm: () -> Unit
        ): ConfirmationDialog {
            return show(
                context = context,
                title = "Delete Confirmation",
                message = "Are you sure you want to delete $itemName? This action cannot be undone.",
                confirmationType = ConfirmationType.DESTRUCTIVE,
                positiveButtonText = "Delete",
                negativeButtonText = "Cancel",
                onConfirm = onConfirm,
                cancellable = false
            )
        }

        /**
         * Show cancel confirmation
         */
        fun showCancelConfirmation(
            context: Context,
            actionName: String = "this action",
            onConfirm: () -> Unit
        ): ConfirmationDialog {
            return show(
                context = context,
                title = "Cancel Confirmation",
                message = "Are you sure you want to cancel $actionName?",
                confirmationType = ConfirmationType.WARNING,
                positiveButtonText = "Yes, Cancel",
                negativeButtonText = "No",
                onConfirm = onConfirm
            )
        }

        /**
         * Show submit confirmation
         */
        fun showSubmitConfirmation(
            context: Context,
            title: String = "Submit Confirmation",
            message: String = "Are you sure you want to submit this information?",
            onConfirm: () -> Unit
        ): ConfirmationDialog {
            return show(
                context = context,
                title = title,
                message = message,
                confirmationType = ConfirmationType.INFO,
                positiveButtonText = "Submit",
                negativeButtonText = "Review",
                onConfirm = onConfirm
            )
        }

        /**
         * Show change PIN confirmation
         */
        fun showChangePinConfirmation(
            context: Context,
            onConfirm: () -> Unit
        ): ConfirmationDialog {
            return show(
                context = context,
                title = "Change PIN",
                message = "Are you sure you want to change your PIN? You will need to remember the new PIN for future logins.",
                confirmationType = ConfirmationType.WARNING,
                positiveButtonText = "Yes, Change",
                negativeButtonText = "Cancel",
                onConfirm = onConfirm
            )
        }

        /**
         * Show fertilizer application confirmation
         */
        fun showFertilizerApplicationConfirmation(
            context: Context,
            season: String,
            quantity: Int,
            onConfirm: () -> Unit
        ): ConfirmationDialog {
            return show(
                context = context,
                title = "Submit Application",
                message = "You are about to apply for $quantity bags of fertilizer for $season season. Do you want to proceed?",
                confirmationType = ConfirmationType.INFO,
                positiveButtonText = "Submit",
                negativeButtonText = "Review",
                onConfirm = onConfirm
            )
        }

        /**
         * Show exit confirmation
         */
        fun showExitConfirmation(
            context: Context,
            message: String = "Are you sure you want to exit? Any unsaved changes will be lost.",
            onConfirm: () -> Unit
        ): ConfirmationDialog {
            return show(
                context = context,
                title = "Exit Confirmation",
                message = message,
                confirmationType = ConfirmationType.WARNING,
                positiveButtonText = "Exit",
                negativeButtonText = "Stay",
                onConfirm = onConfirm
            )
        }

        /**
         * Show discard changes confirmation
         */
        fun showDiscardChangesConfirmation(
            context: Context,
            onConfirm: () -> Unit
        ): ConfirmationDialog {
            return show(
                context = context,
                title = "Discard Changes",
                message = "You have unsaved changes. Are you sure you want to discard them?",
                confirmationType = ConfirmationType.WARNING,
                positiveButtonText = "Discard",
                negativeButtonText = "Keep Editing",
                onConfirm = onConfirm
            )
        }

        /**
         * Show generic Yes/No confirmation
         */
        fun showYesNoConfirmation(
            context: Context,
            title: String,
            message: String,
            onYes: () -> Unit,
            onNo: (() -> Unit)? = null
        ): ConfirmationDialog {
            return show(
                context = context,
                title = title,
                message = message,
                confirmationType = ConfirmationType.NORMAL,
                positiveButtonText = "Yes",
                negativeButtonText = "No",
                onConfirm = onYes,
                onCancel = onNo
            )
        }
    }
}

/**
 * Extension function to show confirmation dialog on Context
 */
fun Context.showConfirmationDialog(
    title: String,
    message: String,
    confirmationType: ConfirmationDialog.ConfirmationType = ConfirmationDialog.ConfirmationType.NORMAL,
    positiveButtonText: String = "Confirm",
    negativeButtonText: String = "Cancel",
    onConfirm: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null
): ConfirmationDialog {
    return ConfirmationDialog.show(
        context = this,
        title = title,
        message = message,
        confirmationType = confirmationType,
        positiveButtonText = positiveButtonText,
        negativeButtonText = negativeButtonText,
        onConfirm = onConfirm,
        onCancel = onCancel
    )
}

/**
 * Extension function for logout confirmation
 */
fun Context.showLogoutConfirmation(onConfirm: () -> Unit): ConfirmationDialog {
    return ConfirmationDialog.showLogoutConfirmation(this, onConfirm)
}

/**
 * Extension function for delete confirmation
 */
fun Context.showDeleteConfirmation(itemName: String = "this item", onConfirm: () -> Unit): ConfirmationDialog {
    return ConfirmationDialog.showDeleteConfirmation(this, itemName, onConfirm)
}

/**
 * Builder class for creating confirmation dialogs with more options
 */
class ConfirmationDialogBuilder(private val context: Context) {
    private var title: String = "Confirmation"
    private var message: String = ""
    private var confirmationType: ConfirmationDialog.ConfirmationType = ConfirmationDialog.ConfirmationType.NORMAL
    private var positiveButtonText: String = "Confirm"
    private var negativeButtonText: String = "Cancel"
    private var onConfirm: (() -> Unit)? = null
    private var onCancel: (() -> Unit)? = null
    private var cancellable: Boolean = true

    fun setTitle(title: String): ConfirmationDialogBuilder {
        this.title = title
        return this
    }

    fun setMessage(message: String): ConfirmationDialogBuilder {
        this.message = message
        return this
    }

    fun setConfirmationType(type: ConfirmationDialog.ConfirmationType): ConfirmationDialogBuilder {
        this.confirmationType = type
        return this
    }

    fun setPositiveButton(text: String, onClick: (() -> Unit)? = null): ConfirmationDialogBuilder {
        this.positiveButtonText = text
        this.onConfirm = onClick
        return this
    }

    fun setNegativeButton(text: String, onClick: (() -> Unit)? = null): ConfirmationDialogBuilder {
        this.negativeButtonText = text
        this.onCancel = onClick
        return this
    }

    fun setCancellable(cancellable: Boolean): ConfirmationDialogBuilder {
        this.cancellable = cancellable
        return this
    }

    fun build(): ConfirmationDialog {
        return ConfirmationDialog(
            context,
            title,
            message,
            confirmationType,
            positiveButtonText,
            negativeButtonText,
            onConfirm,
            onCancel,
            cancellable
        )
    }

    fun show(): ConfirmationDialog {
        val dialog = build()
        dialog.show()
        return dialog
    }
}