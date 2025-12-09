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

        LayoutInflater.from(context).inflate(R.layout.view_empty_state, this, true)

        iconImageView = findViewById(R.id.ivEmptyStateIcon)
        titleTextView = findViewById(R.id.tvEmptyStateTitle)
        messageTextView = findViewById(R.id.tvEmptyStateMessage)
        actionButton = findViewById(R.id.btnEmptyStateAction)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.EmptyStateView)

            val iconRes = typedArray.getResourceId(R.styleable.EmptyStateView_emptyStateIcon, 0)
            if (iconRes != 0) {
                iconImageView.setImageResource(iconRes)
            }

            val title = typedArray.getString(R.styleable.EmptyStateView_emptyStateTitle)
            titleTextView.text = title ?: ""

            val message = typedArray.getString(R.styleable.EmptyStateView_emptyStateMessage)
            messageTextView.text = message ?: ""

            val actionText = typedArray.getString(R.styleable.EmptyStateView_emptyStateActionText)
            if (actionText.isNullOrBlank()) {
                actionButton.visibility = GONE
            } else {
                actionButton.text = actionText
            }

            typedArray.recycle()
        }

        actionButton.setOnClickListener {
            onActionClick?.invoke()
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

    fun showActionButton() {
        actionButton.visibility = VISIBLE
    }

    fun hideActionButton() {
        actionButton.visibility = GONE
    }

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
        fun create(
            context: Context,
            type: EmptyStateType,
            actionClick: (() -> Unit)? = null
        ): EmptyStateView {
            val view = EmptyStateView(context)
            view.setEmptyState(type, actionClick)
            return view
        }

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

fun ViewGroup.showEmptyState(
    type: EmptyStateView.EmptyStateType,
    actionClick: (() -> Unit)? = null
) {
    removeEmptyState()

    val emptyStateView = EmptyStateView.create(context, type, actionClick)
    emptyStateView.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    addView(emptyStateView)
}

fun ViewGroup.showCustomEmptyState(
    iconRes: Int,
    title: String,
    message: String,
    actionText: String? = null,
    actionClick: (() -> Unit)? = null
) {
    removeEmptyState()

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

fun ViewGroup.removeEmptyState() {
    val emptyStateViews = mutableListOf<EmptyStateView>()
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is EmptyStateView) {
            emptyStateViews.add(child)
        }
    }
    emptyStateViews.forEach { removeView(it) }
}

fun ViewGroup.hasEmptyState(): Boolean {
    for (i in 0 until childCount) {
        if (getChildAt(i) is EmptyStateView) {
            return true
        }
    }
    return false
}

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