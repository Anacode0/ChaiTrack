package com.farmer.chaitrack.ui.common.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.farmer.chaitrack.R

/**
 * Custom button component with loading state support
 * Supports icons, loading indicators, and various button styles
 */
class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val buttonContainer: FrameLayout
    private val iconImageView: ImageView
    private val textTextView: TextView
    private val progressBar: ProgressBar

    private var buttonText: String = ""
    private var isLoadingState = false
    private var buttonStyle: ButtonStyle = ButtonStyle.PRIMARY
    private var onClickListener: OnClickListener? = null

    /**
     * Button style types
     */
    enum class ButtonStyle {
        PRIMARY,
        SECONDARY,
        DESTRUCTIVE,
        SUCCESS,
        WARNING,
        TEXT_ONLY
    }

    init {
        // Inflate layout
        LayoutInflater.from(context).inflate(R.layout.view_custom_button, this, true)

        // Initialize views
        buttonContainer = findViewById(R.id.buttonContainer)
        iconImageView = findViewById(R.id.ivButtonIcon)
        textTextView = findViewById(R.id.tvButtonText)
        progressBar = findViewById(R.id.progressBarButton)

        // Load custom attributes
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomButton)

            try {
                // Button text
                buttonText = typedArray.getString(R.styleable.CustomButton_buttonText) ?: ""
                textTextView.text = buttonText

                // Button icon
                val iconDrawable = typedArray.getDrawable(R.styleable.CustomButton_buttonIcon)
                if (iconDrawable != null) {
                    setIcon(iconDrawable)
                } else {
                    iconImageView.visibility = GONE
                }

                // Button style - FIXED: Use the specific attribute name
                val styleIndex = typedArray.getInt(R.styleable.CustomButton_android_text, 0)
                buttonStyle = ButtonStyle.values()[styleIndex]
                applyButtonStyle(buttonStyle)

                // Enabled state
                val isEnabled = typedArray.getBoolean(R.styleable.CustomButton_android_enabled, true)
                setEnabled(isEnabled)

            } finally {
                typedArray.recycle()
            }
        }

        // Setup click handling
        buttonContainer.setOnClickListener {
            if (isEnabled && !isLoadingState) {
                onClickListener?.onClick(this)
            }
        }
    }

    /**
     * Set button text
     */
    fun setText(text: String) {
        buttonText = text
        textTextView.text = text
    }

    /**
     * Set button text from resource
     */
    fun setText(textRes: Int) {
        setText(context.getString(textRes))
    }

    /**
     * Get button text
     */
    fun getText(): String = buttonText

    /**
     * Set button icon
     */
    fun setIcon(drawable: Drawable?) {
        if (drawable != null) {
            iconImageView.visibility = VISIBLE
            iconImageView.setImageDrawable(drawable)
        } else {
            iconImageView.visibility = GONE
        }
    }

    /**
     * Set button icon from resource
     */
    fun setIcon(iconRes: Int) {
        setIcon(ContextCompat.getDrawable(context, iconRes))
    }

    /**
     * Remove button icon
     */
    fun removeIcon() {
        iconImageView.visibility = GONE
    }

    /**
     * Set button style
     */
    fun setButtonStyle(style: ButtonStyle) {
        buttonStyle = style
        applyButtonStyle(style)
    }

    /**
     * Apply button style
     */
    private fun applyButtonStyle(style: ButtonStyle) {
        val backgroundRes = when (style) {
            ButtonStyle.PRIMARY -> R.drawable.bg_button_primary
            ButtonStyle.SECONDARY -> R.drawable.bg_button_secondary
            ButtonStyle.DESTRUCTIVE -> R.drawable.bg_button_destructive
            ButtonStyle.SUCCESS -> R.drawable.bg_button_success
            ButtonStyle.WARNING -> R.drawable.bg_button_warning
            ButtonStyle.TEXT_ONLY -> R.drawable.bg_button_text
        }

        buttonContainer.setBackgroundResource(backgroundRes)

        // Set text color based on style
        val textColorRes = when (style) {
            ButtonStyle.SECONDARY, ButtonStyle.TEXT_ONLY -> R.color.button_text_secondary
            else -> R.color.button_text_primary
        }
        textTextView.setTextColor(ContextCompat.getColor(context, textColorRes))
    }

    /**
     * Set loading state
     */
    fun setLoading(loading: Boolean) {
        isLoadingState = loading

        if (loading) {
            // Show progress bar, hide text and icon
            progressBar.visibility = VISIBLE
            textTextView.visibility = INVISIBLE
            iconImageView.visibility = INVISIBLE
            isEnabled = false
        } else {
            // Hide progress bar, show text and icon
            progressBar.visibility = GONE
            textTextView.visibility = VISIBLE
            if (iconImageView.drawable != null) {
                iconImageView.visibility = VISIBLE
            }
            isEnabled = true
        }
    }

    /**
     * Check if button is in loading state
     */
    fun isLoading(): Boolean = isLoadingState

    /**
     * Set enabled state with alpha
     */
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        buttonContainer.isEnabled = enabled
        alpha = if (enabled) 1f else 0.5f
    }

    /**
     * Set click listener
     */
    override fun setOnClickListener(listener: OnClickListener?) {
        onClickListener = listener
    }

    /**
     * Perform click programmatically
     */
    override fun performClick(): Boolean {
        if (isEnabled && !isLoadingState) {
            onClickListener?.onClick(this)
            return true
        }
        return false
    }

    companion object {
        /**
         * Create a primary button
         */
        fun createPrimary(context: Context, text: String): CustomButton {
            val button = CustomButton(context)
            button.setText(text)
            button.setButtonStyle(ButtonStyle.PRIMARY)
            return button
        }

        /**
         * Create a secondary button
         */
        fun createSecondary(context: Context, text: String): CustomButton {
            val button = CustomButton(context)
            button.setText(text)
            button.setButtonStyle(ButtonStyle.SECONDARY)
            return button
        }

        /**
         * Create a destructive button
         */
        fun createDestructive(context: Context, text: String): CustomButton {
            val button = CustomButton(context)
            button.setText(text)
            button.setButtonStyle(ButtonStyle.DESTRUCTIVE)
            return button
        }

        /**
         * Create a success button
         */
        fun createSuccess(context: Context, text: String): CustomButton {
            val button = CustomButton(context)
            button.setText(text)
            button.setButtonStyle(ButtonStyle.SUCCESS)
            return button
        }

        /**
         * Create a text-only button
         */
        fun createTextButton(context: Context, text: String): CustomButton {
            val button = CustomButton(context)
            button.setText(text)
            button.setButtonStyle(ButtonStyle.TEXT_ONLY)
            return button
        }
    }
}

/**
 * Extension function to set click listener with lambda
 */
fun CustomButton.onClick(action: () -> Unit) {
    setOnClickListener { action() }
}

/**
 * Extension function to show loading with optional text
 */
fun CustomButton.showLoading(loadingText: String? = null) {
    loadingText?.let { setText(it) }
    setLoading(true)
}

/**
 * Extension function to hide loading and restore text
 */
fun CustomButton.hideLoading(originalText: String? = null) {
    setLoading(false)
    originalText?.let { setText(it) }
}

/**
 * Extension function to execute action with loading state
 */
suspend fun CustomButton.executeWithLoading(
    loadingText: String? = null,
    action: suspend () -> Unit
) {
    val originalText = getText()
    showLoading(loadingText)

    try {
        action()
    } finally {
        hideLoading(originalText)
    }
}

/**
 * Builder class for CustomButton
 */
class CustomButtonBuilder(private val context: Context) {
    private var text: String = ""
    private var icon: Drawable? = null
    private var style: CustomButton.ButtonStyle = CustomButton.ButtonStyle.PRIMARY
    private var enabled: Boolean = true
    private var clickListener: (() -> Unit)? = null

    fun setText(text: String): CustomButtonBuilder {
        this.text = text
        return this
    }

    fun setText(textRes: Int): CustomButtonBuilder {
        this.text = context.getString(textRes)
        return this
    }

    fun setIcon(drawable: Drawable): CustomButtonBuilder {
        this.icon = drawable
        return this
    }

    fun setIcon(iconRes: Int): CustomButtonBuilder {
        this.icon = ContextCompat.getDrawable(context, iconRes)
        return this
    }

    fun setStyle(style: CustomButton.ButtonStyle): CustomButtonBuilder {
        this.style = style
        return this
    }

    fun setEnabled(enabled: Boolean): CustomButtonBuilder {
        this.enabled = enabled
        return this
    }

    fun setOnClickListener(listener: () -> Unit): CustomButtonBuilder {
        this.clickListener = listener
        return this
    }

    fun build(): CustomButton {
        val button = CustomButton(context)
        button.setText(text)
        button.setIcon(icon)
        button.setButtonStyle(style)
        button.setEnabled(enabled)
        clickListener?.let { button.onClick(it) }
        return button
    }
}

/**
 * DSL function to create CustomButton
 */
fun Context.customButton(block: CustomButtonBuilder.() -> Unit): CustomButton {
    return CustomButtonBuilder(this).apply(block).build()
}

/**
 * Extension functions for common button configurations
 */
fun CustomButton.asPrimary(): CustomButton {
    setButtonStyle(CustomButton.ButtonStyle.PRIMARY)
    return this
}

fun CustomButton.asSecondary(): CustomButton {
    setButtonStyle(CustomButton.ButtonStyle.SECONDARY)
    return this
}

fun CustomButton.asDestructive(): CustomButton {
    setButtonStyle(CustomButton.ButtonStyle.DESTRUCTIVE)
    return this
}

fun CustomButton.asSuccess(): CustomButton {
    setButtonStyle(CustomButton.ButtonStyle.SUCCESS)
    return this
}

fun CustomButton.asWarning(): CustomButton {
    setButtonStyle(CustomButton.ButtonStyle.WARNING)
    return this
}

fun CustomButton.asTextOnly(): CustomButton {
    setButtonStyle(CustomButton.ButtonStyle.TEXT_ONLY)
    return this
}