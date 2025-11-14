package com.farmer.chaitrack.ui.common.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.farmer.chaitrack.R
import com.farmer.chaitrack.util.Result

/**
 * Reusable error state view component
 * Shows when an error occurs loading content
 * Displays an icon, error message, and action buttons (typically retry)
 */
class ErrorStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val iconImageView: ImageView
    private val titleTextView: TextView
    private val messageTextView: TextView
    private val primaryButton: Button
    private val secondaryButton: Button

    private var onPrimaryClick: (() -> Unit)? = null
    private var onSecondaryClick: (() -> Unit)? = null

    /**
     * Predefined error state types
     */
    enum class ErrorStateType {
        NETWORK_ERROR,      // No internet connection
        SERVER_ERROR,       // Server/API error
        TIMEOUT_ERROR,      // Request timeout
        NOT_FOUND,          // Resource not found
        UNAUTHORIZED,       // Authentication error
        GENERIC_ERROR       // Generic error
    }

    init {
        orientation = VERTICAL
        gravity = android.view.Gravity.CENTER

        // Inflate layout
        LayoutInflater.from(context).inflate(R.layout.view_error_state, this, true)

        // Initialize views
        iconImageView = findViewById(R.id.ivErrorStateIcon)
        titleTextView = findViewById(R.id.tvErrorStateTitle)
        messageTextView = findViewById(R.id.tvErrorStateMessage)
        primaryButton = findViewById(R.id.btnErrorStatePrimary)
        secondaryButton = findViewById(R.id.btnErrorStateSecondary)

        // Load custom attributes if provided
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ErrorStateView)

            // Icon
            val iconRes = typedArray.getResourceId(R.styleable.ErrorStateView_errorStateIcon, 0)
            if (iconRes != 0) {
                iconImageView.setImageResource(iconRes)
            }

            // Title
            val title = typedArray.getString(R.styleable.ErrorStateView_errorStateTitle)
            titleTextView.text = title ?: "Error"

            // Message
            val message = typedArray.getString(R.styleable.ErrorStateView_errorStateMessage)
            messageTextView.text = message ?: "Something went wrong"

            // Primary button text
            val primaryText = typedArray.getString(R.styleable.ErrorStateView_errorStatePrimaryText)
            if (primaryText.isNullOrBlank()) {
                primaryButton.visibility = GONE
            } else {
                primaryButton.text = primaryText
            }

            // Secondary button text
            val secondaryText = typedArray.getString(R.styleable.ErrorStateView_errorStateSecondaryText)
            if (secondaryText.isNullOrBlank()) {
                secondaryButton.visibility = GONE
            } else {
                secondaryButton.text = secondaryText
            }

            typedArray.recycle()
        }

        // Setup buttons
        primaryButton.setOnClickListener {
            onPrimaryClick?.invoke()
        }

        secondaryButton.setOnClickListener {
            onSecondaryClick?.invoke()
        }
    }

    /**
     * Set the icon resource
     */
    fun setIcon(iconRes: Int) {
        iconImageView.setImageResource(iconRes)
    }

    /**
     * Set the title text
     */
    fun setTitle(title: String) {
        titleTextView.text = title
        titleTextView.visibility = if (title.isBlank()) GONE else VISIBLE
    }

    /**
     * Set the message text
     */
    fun setMessage(message: String) {
        messageTextView.text = message
        messageTextView.visibility = if (message.isBlank()) GONE else VISIBLE
    }

    /**
     * Set the primary button text and click listener
     */
    fun setPrimaryButton(text: String?, onClick: (() -> Unit)? = null) {
        if (text.isNullOrBlank()) {
            primaryButton.visibility = GONE
            onPrimaryClick = null
        } else {
            primaryButton.visibility = VISIBLE
            primaryButton.text = text
            onPrimaryClick = onClick
        }
    }

    /**
     * Set the secondary button text and click listener
     */
    fun setSecondaryButton(text: String?, onClick: (() -> Unit)? = null) {
        if (text.isNullOrBlank()) {
            secondaryButton.visibility = GONE
            onSecondaryClick = null
        } else {
            secondaryButton.visibility = VISIBLE
            secondaryButton.text = text
            onSecondaryClick = onClick
        }
    }

    /**
     * Show primary button
     */
    fun showPrimaryButton() {
        primaryButton.visibility = VISIBLE
    }

    /**
     * Hide primary button
     */
    fun hidePrimaryButton() {
        primaryButton.visibility = GONE
    }

    /**
     * Show secondary button
     */
    fun showSecondaryButton() {
        secondaryButton.visibility = VISIBLE
    }

    /**
     * Hide secondary button
     */
    fun hideSecondaryButton() {
        secondaryButton.visibility = GONE
    }

    /**
     * Configure error state with predefined type
     */
    fun setErrorState(
        type: ErrorStateType,
        onRetry: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null
    ) {
        when (type) {
            ErrorStateType.NETWORK_ERROR -> {
                setIcon(R.drawable.ic_no_network)
                setTitle("No Internet Connection")
                setMessage("Please check your connection and try again.")
                setPrimaryButton("Retry", onRetry)
                setSecondaryButton("Dismiss", onDismiss)
            }

            ErrorStateType.SERVER_ERROR -> {
                setIcon(R.drawable.ic_server_error)
                setTitle("Server Error")
                setMessage("Something went wrong on our end. Please try again later.")
                setPrimaryButton("Retry", onRetry)
                setSecondaryButton("Cancel", onDismiss)
            }

            ErrorStateType.TIMEOUT_ERROR -> {
                setIcon(R.drawable.ic_timeout)
                setTitle("Request Timeout")
                setMessage("The request took too long to complete. Please check your connection and try again.")
                setPrimaryButton("Retry", onRetry)
                setSecondaryButton("Cancel", onDismiss)
            }

            ErrorStateType.NOT_FOUND -> {
                setIcon(R.drawable.ic_not_found)
                setTitle("Not Found")
                setMessage("The requested information could not be found.")
                setPrimaryButton("Go Back", onDismiss)
                setSecondaryButton(null)
            }

            ErrorStateType.UNAUTHORIZED -> {
                setIcon(R.drawable.ic_lock_error)
                setTitle("Authentication Required")
                setMessage("Your session has expired. Please login again.")
                setPrimaryButton("Login", onRetry)
                setSecondaryButton("Cancel", onDismiss)
            }

            ErrorStateType.GENERIC_ERROR -> {
                setIcon(R.drawable.ic_error)
                setTitle("Something Went Wrong")
                setMessage("An unexpected error occurred. Please try again.")
                setPrimaryButton("Retry", onRetry)
                setSecondaryButton("Cancel", onDismiss)
            }
        }
    }

    /**
     * Configure error state from Result.Error
     */
    fun setErrorFromResult(
        error: Result.Error,
        onRetry: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null
    ) {
        val errorType = when (error.code) {
            Result.ErrorCode.NETWORK_ERROR -> ErrorStateType.NETWORK_ERROR
            Result.ErrorCode.SERVER_ERROR -> ErrorStateType.SERVER_ERROR
            Result.ErrorCode.TIMEOUT -> ErrorStateType.TIMEOUT_ERROR
            Result.ErrorCode.NOT_FOUND -> ErrorStateType.NOT_FOUND
            Result.ErrorCode.AUTHENTICATION_ERROR,
            Result.ErrorCode.AUTHORIZATION_ERROR -> ErrorStateType.UNAUTHORIZED
            else -> ErrorStateType.GENERIC_ERROR
        }

        setErrorState(errorType, onRetry, onDismiss)
        // Override message with specific error message
        setMessage(error.message)
    }

    /**
     * Configure custom error state
     */
    fun setCustomErrorState(
        iconRes: Int,
        title: String,
        message: String,
        primaryText: String? = "Retry",
        secondaryText: String? = null,
        onPrimaryClick: (() -> Unit)? = null,
        onSecondaryClick: (() -> Unit)? = null
    ) {
        setIcon(iconRes)
        setTitle(title)
        setMessage(message)
        setPrimaryButton(primaryText, onPrimaryClick)
        setSecondaryButton(secondaryText, onSecondaryClick)
    }

    companion object {
        /**
         * Create an error state view programmatically
         */
        fun create(
            context: Context,
            type: ErrorStateType,
            onRetry: (() -> Unit)? = null,
            onDismiss: (() -> Unit)? = null
        ): ErrorStateView {
            val view = ErrorStateView(context)
            view.setErrorState(type, onRetry, onDismiss)
            return view
        }

        /**
         * Create error state from Result.Error
         */
        fun createFromResult(
            context: Context,
            error: Result.Error,
            onRetry: (() -> Unit)? = null,
            onDismiss: (() -> Unit)? = null
        ): ErrorStateView {
            val view = ErrorStateView(context)
            view.setErrorFromResult(error, onRetry, onDismiss)
            return view
        }

        /**
         * Create a custom error state view
         */
        fun createCustom(
            context: Context,
            iconRes: Int,
            title: String,
            message: String,
            primaryText: String? = "Retry",
            secondaryText: String? = null,
            onPrimaryClick: (() -> Unit)? = null,
            onSecondaryClick: (() -> Unit)? = null
        ): ErrorStateView {
            val view = ErrorStateView(context)
            view.setCustomErrorState(
                iconRes,
                title,
                message,
                primaryText,
                secondaryText,
                onPrimaryClick,
                onSecondaryClick
            )
            return view
        }
    }
}

/**
 * Extension function to show error state in a view group
 */
fun android.view.ViewGroup.showErrorState(
    type: ErrorStateView.ErrorStateType,
    onRetry: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    // Remove any existing error state views
    removeErrorState()

    // Add new error state view
    val errorStateView = ErrorStateView.create(context, type, onRetry, onDismiss)
    errorStateView.layoutParams = android.view.ViewGroup.LayoutParams(
        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
        android.view.ViewGroup.LayoutParams.MATCH_PARENT
    )
    addView(errorStateView)
}

/**
 * Extension function to show error state from Result.Error
 */
fun android.view.ViewGroup.showErrorFromResult(
    error: Result.Error,
    onRetry: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    // Remove any existing error state views
    removeErrorState()

    // Add new error state view
    val errorStateView = ErrorStateView.createFromResult(context, error, onRetry, onDismiss)
    errorStateView.layoutParams = android.view.ViewGroup.LayoutParams(
        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
        android.view.ViewGroup.LayoutParams.MATCH_PARENT
    )
    addView(errorStateView)
}

/**
 * Extension function to show custom error state in a view group
 */
fun android.view.ViewGroup.showCustomErrorState(
    iconRes: Int,
    title: String,
    message: String,
    primaryText: String? = "Retry",
    secondaryText: String? = null,
    onPrimaryClick: (() -> Unit)? = null,
    onSecondaryClick: (() -> Unit)? = null
) {
    // Remove any existing error state views
    removeErrorState()

    // Add new error state view
    val errorStateView = ErrorStateView.createCustom(
        context,
        iconRes,
        title,
        message,
        primaryText,
        secondaryText,
        onPrimaryClick,
        onSecondaryClick
    )
    errorStateView.layoutParams = android.view.ViewGroup.LayoutParams(
        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
        android.view.ViewGroup.LayoutParams.MATCH_PARENT
    )
    addView(errorStateView)
}

/**
 * Extension function to remove error state from a view group
 */
fun android.view.ViewGroup.removeErrorState() {
    // Find and remove all ErrorStateView instances
    val errorStateViews = mutableListOf<ErrorStateView>()
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is ErrorStateView) {
            errorStateViews.add(child)
        }
    }
    errorStateViews.forEach { removeView(it) }
}

/**
 * Extension function to check if view group has error state
 */
fun android.view.ViewGroup.hasErrorState(): Boolean {
    for (i in 0 until childCount) {
        if (getChildAt(i) is ErrorStateView) {
            return true
        }
    }
    return false
}

/**
 * Extension function to handle Result states and show appropriate views
 */
fun <T> android.view.ViewGroup.handleResultState(
    result: Result<T>,
    contentView: android.view.View? = null,
    loadingView: android.view.View? = null,
    onRetry: (() -> Unit)? = null,
    onSuccess: ((T) -> Unit)? = null
) {
    when (result) {
        is Result.Loading -> {
            contentView?.visibility = android.view.View.GONE
            loadingView?.visibility = android.view.View.VISIBLE
            removeErrorState()
        }

        is Result.Success -> {
            contentView?.visibility = android.view.View.VISIBLE
            loadingView?.visibility = android.view.View.GONE
            removeErrorState()
            onSuccess?.invoke(result.data)
        }

        is Result.Error -> {
            contentView?.visibility = android.view.View.GONE
            loadingView?.visibility = android.view.View.GONE
            showErrorFromResult(result, onRetry)
        }
    }
}