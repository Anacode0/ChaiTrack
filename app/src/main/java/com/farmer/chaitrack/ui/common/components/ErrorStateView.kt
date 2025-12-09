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

    enum class ErrorStateType {
        NETWORK_ERROR,
        SERVER_ERROR,
        TIMEOUT_ERROR,
        NOT_FOUND,
        UNAUTHORIZED,
        GENERIC_ERROR
    }

    init {
        orientation = VERTICAL
        gravity = android.view.Gravity.CENTER

        LayoutInflater.from(context).inflate(R.layout.view_error_state, this, true)

        iconImageView = findViewById(R.id.ivErrorStateIcon)
        titleTextView = findViewById(R.id.tvErrorStateTitle)
        messageTextView = findViewById(R.id.tvErrorStateMessage)
        primaryButton = findViewById(R.id.btnErrorStatePrimary)
        secondaryButton = findViewById(R.id.btnErrorStateSecondary)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ErrorStateView)

            val iconRes = typedArray.getResourceId(R.styleable.ErrorStateView_errorStateIcon, 0)
            if (iconRes != 0) {
                iconImageView.setImageResource(iconRes)
            }

            val title = typedArray.getString(R.styleable.ErrorStateView_errorStateTitle)
            titleTextView.text = title ?: "Error"

            val message = typedArray.getString(R.styleable.ErrorStateView_errorStateMessage)
            messageTextView.text = message ?: "Something went wrong"

            val primaryText = typedArray.getString(R.styleable.ErrorStateView_errorStatePrimaryText)
            if (primaryText.isNullOrBlank()) {
                primaryButton.visibility = GONE
            } else {
                primaryButton.text = primaryText
            }

            val secondaryText = typedArray.getString(R.styleable.ErrorStateView_errorStateSecondaryText)
            if (secondaryText.isNullOrBlank()) {
                secondaryButton.visibility = GONE
            } else {
                secondaryButton.text = secondaryText
            }

            typedArray.recycle()
        }

        primaryButton.setOnClickListener {
            onPrimaryClick?.invoke()
        }

        secondaryButton.setOnClickListener {
            onSecondaryClick?.invoke()
        }
    }

    fun setIcon(iconRes: Int) {
        iconImageView.setImageResource(iconRes)
    }

    fun setTitle(title: String) {
        titleTextView.text = title
        titleTextView.visibility = if (title.isBlank()) GONE else VISIBLE
    }

    fun setMessage(message: String) {
        messageTextView.text = message
        messageTextView.visibility = if (message.isBlank()) GONE else VISIBLE
    }

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

    fun showPrimaryButton() {
        primaryButton.visibility = VISIBLE
    }

    fun hidePrimaryButton() {
        primaryButton.visibility = GONE
    }

    fun showSecondaryButton() {
        secondaryButton.visibility = VISIBLE
    }

    fun hideSecondaryButton() {
        secondaryButton.visibility = GONE
    }

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
        setMessage(error.message)
    }
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

fun android.view.ViewGroup.showErrorState(
    type: ErrorStateView.ErrorStateType,
    onRetry: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    removeErrorState()

    val errorStateView = ErrorStateView.create(context, type, onRetry, onDismiss)
    errorStateView.layoutParams = android.view.ViewGroup.LayoutParams(
        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
        android.view.ViewGroup.LayoutParams.MATCH_PARENT
    )
    addView(errorStateView)
}

fun android.view.ViewGroup.showErrorFromResult(
    error: Result.Error,
    onRetry: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    removeErrorState()

    val errorStateView = ErrorStateView.createFromResult(context, error, onRetry, onDismiss)
    errorStateView.layoutParams = android.view.ViewGroup.LayoutParams(
        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
        android.view.ViewGroup.LayoutParams.MATCH_PARENT
    )
    addView(errorStateView)
}

fun android.view.ViewGroup.showCustomErrorState(
    iconRes: Int,
    title: String,
    message: String,
    primaryText: String? = "Retry",
    secondaryText: String? = null,
    onPrimaryClick: (() -> Unit)? = null,
    onSecondaryClick: (() -> Unit)? = null
) {
    removeErrorState()

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

fun android.view.ViewGroup.removeErrorState() {
    val errorStateViews = mutableListOf<ErrorStateView>()
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is ErrorStateView) {
            errorStateViews.add(child)
        }
    }
    errorStateViews.forEach { removeView(it) }
}

fun android.view.ViewGroup.hasErrorState(): Boolean {
    for (i in 0 until childCount) {
        if (getChildAt(i) is ErrorStateView) {
            return true
        }
    }
    return false
}

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