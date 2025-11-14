package com.farmer.chaitrack.ui.common.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.farmer.chaitrack.R

/**
 * Reusable empty state view component
 * Shows when lists or content areas are empty
 * Displays an icon, message, and optional action button
 */
class EmptyStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val iconImageView: ImageView
    private val titleTextView: TextView
    private val messageTextView: TextView
    private val actionButton: Button

    private var onActionClick: (() -> Unit)? = null

    /**
     * Predefined empty state types
     */
    enum class EmptyStateType {
        NO_DELIVERIES,
        NO_FERTILIZER_APPLICATIONS,
        NO_NOTIFICATIONS,
        NO_ANNOUNCEMENTS,
        NO_FINANCE_RECORDS,
        NO_SEARCH_RESULTS,
        NO_INTERNET,
        GENERIC
    }

    init {
        orientation = VERTICAL
        gravity = android.view.Gravity.CENTER

        // Inflate layout
        LayoutInflater.from(context).inflate(R.layout.view_empty_state, this, true)

        // Initialize views
        iconImageView = findViewById(R.id.ivEmptyStateIcon)
        titleTextView = findViewById(R.id.tvEmptyStateTitle)
        messageTextView = findViewById(R.id.tvEmptyStateMessage)
        actionButton = findViewById(R.id.btnEmptyStateAction)

        // Load custom attributes if provided
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.EmptyStateView)

            // Icon
            val iconRes = typedArray.getResourceId(R.styleable.EmptyStateView_emptyStateIcon, 0)
            if (iconRes != 0) {
                iconImageView.setImageResource(iconRes)
            }

            // Title
            val title = typedArray.getString(R.styleable.EmptyStateView_emptyStateTitle)
            titleTextView.text = title ?: ""

            // Message
            val message = typedArray.getString(R.styleable.EmptyStateView_emptyStateMessage)
            messageTextView.text = message ?: ""

            // Action button text
            val actionText = typedArray.getString(R.styleable.EmptyStateView_emptyStateActionText)
            if (actionText.isNullOrBlank()) {
                actionButton.visibility = GONE
            } else {
                actionButton.text = actionText
            }

            typedArray.recycle()
        }

        // Setup action button
        actionButton.setOnClickListener {
            onActionClick?.invoke()
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
     * Set the action button text and click listener
     */
    fun setActionButton(text: String?, onClick: (() -> Unit)? = null) {
        if (text.isNullOrBlank()) {
            actionButton.visibility = GONE
            onActionClick = null
        } else {
            actionButton.visibility = VISIBLE
            actionButton.text = text
            onActionClick = onClick
        }
    }

    /**
     * Show action button
     */
    fun showActionButton() {
        actionButton.visibility = VISIBLE
    }

    /**
     * Hide action button
     */
    fun hideActionButton() {
        actionButton.visibility = GONE
    }

    /**
     * Configure empty state with predefined type
     */
    fun setEmptyState(type: EmptyStateType, actionClick: (() -> Unit)? = null) {
        when (type) {
            EmptyStateType.NO_DELIVERIES -> {
                setIcon(R.drawable.ic_no_deliveries)
                setTitle("No Deliveries Yet")
                setMessage("Your delivery history will appear here once you start delivering tea to the factory.")
                setActionButton(null)
            }

            EmptyStateType.NO_FERTILIZER_APPLICATIONS -> {
                setIcon(R.drawable.ic_no_fertilizer)
                setTitle("No Applications")
                setMessage("You haven't applied for fertilizer yet. Applications will be available during the application period.")
                setActionButton("Apply Now", actionClick)
            }

            EmptyStateType.NO_NOTIFICATIONS -> {
                setIcon(R.drawable.ic_no_notifications)
                setTitle("No Notifications")
                setMessage("You're all caught up! You'll see important updates and announcements here.")
                setActionButton(null)
            }

            EmptyStateType.NO_ANNOUNCEMENTS -> {
                setIcon(R.drawable.ic_no_announcements)
                setTitle("No Announcements")
                setMessage("There are no announcements at the moment. Check back later for updates from the factory.")
                setActionButton(null)
            }

            EmptyStateType.NO_FINANCE_RECORDS -> {
                setIcon(R.drawable.ic_no_records)
                setTitle("No Finance Records")
                setMessage("Your salary transcripts and payment history will appear here once they are available.")
                setActionButton(null)
            }

            EmptyStateType.NO_SEARCH_RESULTS -> {
                setIcon(R.drawable.ic_no_search_results)
                setTitle("No Results Found")
                setMessage("We couldn't find any results matching your search. Try adjusting your filters or search terms.")
                setActionButton("Clear Filters", actionClick)
            }

            EmptyStateType.NO_INTERNET -> {
                setIcon(R.drawable.ic_no_network)
                setTitle("No Internet Connection")
                setMessage("Please check your connection and try again. Some features require an active internet connection.")
                setActionButton("Retry", actionClick)
            }

            EmptyStateType.GENERIC -> {
                setIcon(R.drawable.ic_empty_box)
                setTitle("Nothing Here")
                setMessage("There's nothing to display at the moment.")
                setActionButton(null)
            }
        }
    }

    /**
     * Configure custom empty state
     */
    fun setCustomEmptyState(
        iconRes: Int,
        title: String,
        message: String,
        actionText: String? = null,
        actionClick: (() -> Unit)? = null
    ) {
        setIcon(iconRes)
        setTitle(title)
        setMessage(message)
        setActionButton(actionText, actionClick)
    }

    companion object {
        /**
         * Create an empty state view programmatically
         */
        fun create(
            context: Context,
            type: EmptyStateType,
            actionClick: (() -> Unit)? = null
        ): EmptyStateView {
            val view = EmptyStateView(context)
            view.setEmptyState(type, actionClick)
            return view
        }

        /**
         * Create a custom empty state view
         */
        fun createCustom(
            context: Context,
            iconRes: Int,
            title: String,
            message: String,
            actionText: String? = null,
            actionClick: (() -> Unit)? = null
        ): EmptyStateView {
            val view = EmptyStateView(context)
            view.setCustomEmptyState(iconRes, title, message, actionText, actionClick)
            return view
        }
    }
}

/**
 * Extension function to show empty state in a view group
 */
fun ViewGroup.showEmptyState(
    type: EmptyStateView.EmptyStateType,
    actionClick: (() -> Unit)? = null
) {
    // Remove any existing empty state views
    removeEmptyState()

    // Add new empty state view
    val emptyStateView = EmptyStateView.create(context, type, actionClick)
    emptyStateView.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    addView(emptyStateView)
}

/**
 * Extension function to show custom empty state in a view group
 */
fun ViewGroup.showCustomEmptyState(
    iconRes: Int,
    title: String,
    message: String,
    actionText: String? = null,
    actionClick: (() -> Unit)? = null
) {
    // Remove any existing empty state views
    removeEmptyState()

    // Add new empty state view
    val emptyStateView = EmptyStateView.createCustom(
        context,
        iconRes,
        title,
        message,
        actionText,
        actionClick
    )
    emptyStateView.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    addView(emptyStateView)
}

/**
 * Extension function to remove empty state from a view group
 */
fun ViewGroup.removeEmptyState() {
    // Find and remove all EmptyStateView instances
    val emptyStateViews = mutableListOf<EmptyStateView>()
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is EmptyStateView) {
            emptyStateViews.add(child)
        }
    }
    emptyStateViews.forEach { removeView(it) }
}

/**
 * Extension function to check if view group has empty state
 */
fun ViewGroup.hasEmptyState(): Boolean {
    for (i in 0 until childCount) {
        if (getChildAt(i) is EmptyStateView) {
            return true
        }
    }
    return false
}

/**
 * Extension function to toggle between content and empty state
 */
fun ViewGroup.toggleEmptyState(
    isEmpty: Boolean,
    type: EmptyStateView.EmptyStateType,
    contentView: android.view.View? = null,
    actionClick: (() -> Unit)? = null
) {
    if (isEmpty) {
        contentView?.visibility = android.view.View.GONE
        showEmptyState(type, actionClick)
    } else {
        contentView?.visibility = android.view.View.VISIBLE
        removeEmptyState()
    }
}