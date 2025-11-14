package com.farmer.chaitrack.ui.common.components

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.farmer.chaitrack.R

/**
 * Custom PIN input view component
 * Displays individual boxes for each PIN digit with masking
 * Supports 5-10 digit PINs as per app requirements
 */
class PinInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val pinBoxes = mutableListOf<TextView>()
    private val hiddenEditText: EditText
    private val errorTextView: TextView

    private var pinLength: Int = 6
    private var isMasked: Boolean = true
    private var currentPin: String = ""

    private var onPinEnteredListener: ((String) -> Unit)? = null
    private var onPinChangedListener: ((String, Boolean) -> Unit)? = null

    init {
        orientation = VERTICAL

        // Inflate layout
        LayoutInflater.from(context).inflate(R.layout.view_pin_input, this, true)

        // Get container and error text
        val pinContainer: LinearLayout = findViewById(R.id.pinContainer)
        errorTextView = findViewById(R.id.tvPinError)
        hiddenEditText = findViewById(R.id.hiddenEditText)

        // Load custom attributes
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.PinInputView)

            pinLength = typedArray.getInt(R.styleable.PinInputView_pinLength, 6)
            isMasked = typedArray.getBoolean(R.styleable.PinInputView_masked, true)

            typedArray.recycle()
        }

        // Validate PIN length (5-10 as per requirements)
        if (pinLength < 5 || pinLength > 10) {
            pinLength = 6
        }

        // Setup hidden EditText
        setupHiddenEditText()

        // Create PIN boxes
        createPinBoxes(pinContainer)

        // Focus on hidden EditText when container is clicked
        pinContainer.setOnClickListener {
            requestPinFocus()
        }
    }

    /**
     * Setup the hidden EditText that captures input
     */
    private fun setupHiddenEditText() {
        hiddenEditText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        hiddenEditText.filters = arrayOf(InputFilter.LengthFilter(pinLength))

        hiddenEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentPin = s.toString()
                updatePinBoxes()
                clearError()

                val isComplete = currentPin.length == pinLength
                onPinChangedListener?.invoke(currentPin, isComplete)

                if (isComplete) {
                    onPinEnteredListener?.invoke(currentPin)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * Create individual PIN boxes
     */
    private fun createPinBoxes(container: LinearLayout) {
        container.removeAllViews()
        pinBoxes.clear()

        for (i in 0 until pinLength) {
            val pinBox = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    resources.getDimensionPixelSize(R.dimen.pin_box_size)
                ).apply {
                    weight = 1f
                    marginStart = if (i > 0) resources.getDimensionPixelSize(R.dimen.pin_box_spacing) else 0
                }

                gravity = Gravity.CENTER
                textSize = 24f
                setTextColor(ContextCompat.getColor(context, R.color.pin_text_color))
                setBackgroundResource(R.drawable.bg_pin_box_empty)
            }

            pinBoxes.add(pinBox)
            container.addView(pinBox)
        }
    }

    /**
     * Update PIN boxes with current input
     */
    private fun updatePinBoxes() {
        for (i in 0 until pinLength) {
            val pinBox = pinBoxes[i]

            if (i < currentPin.length) {
                // Box has a digit
                pinBox.text = if (isMasked) "●" else currentPin[i].toString()
                pinBox.setBackgroundResource(R.drawable.bg_pin_box_filled)
            } else if (i == currentPin.length) {
                // Current input box (cursor position)
                pinBox.text = ""
                pinBox.setBackgroundResource(R.drawable.bg_pin_box_active)
            } else {
                // Empty box
                pinBox.text = ""
                pinBox.setBackgroundResource(R.drawable.bg_pin_box_empty)
            }
        }
    }

    /**
     * Set PIN length (5-10 digits)
     */
    fun setPinLength(length: Int) {
        if (length in 5..10) {
            this.pinLength = length
            hiddenEditText.filters = arrayOf(InputFilter.LengthFilter(length))

            // Recreate boxes
            val container: LinearLayout = findViewById(R.id.pinContainer)
            createPinBoxes(container)

            // Clear current PIN if it exceeds new length
            if (currentPin.length > length) {
                clearPin()
            } else {
                updatePinBoxes()
            }
        }
    }

    /**
     * Set whether PIN should be masked
     */
    fun setMasked(masked: Boolean) {
        this.isMasked = masked
        updatePinBoxes()
    }

    /**
     * Get current PIN
     */
    fun getPin(): String = currentPin

    /**
     * Set PIN programmatically
     */
    fun setPin(pin: String) {
        if (pin.length <= pinLength && pin.matches(Regex("^[0-9]*$"))) {
            hiddenEditText.setText(pin)
            hiddenEditText.setSelection(pin.length)
        }
    }

    /**
     * Clear PIN
     */
    fun clearPin() {
        hiddenEditText.text?.clear()
        currentPin = ""
        updatePinBoxes()
    }

    /**
     * Check if PIN is complete
     */
    fun isPinComplete(): Boolean = currentPin.length == pinLength

    /**
     * Validate PIN
     */
    fun validatePin(): Boolean {
        return when {
            currentPin.isEmpty() -> {
                showError("Please enter your PIN")
                false
            }
            currentPin.length < 5 -> {
                showError("PIN must be at least 5 digits")
                false
            }
            currentPin.length < pinLength -> {
                showError("Please complete your PIN")
                false
            }
            !currentPin.matches(Regex("^[0-9]+$")) -> {
                showError("PIN must contain only numbers")
                false
            }
            else -> {
                clearError()
                true
            }
        }
    }

    /**
     * Show error message
     */
    fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE

        // Highlight boxes with error
        pinBoxes.forEach { box ->
            box.setBackgroundResource(R.drawable.bg_pin_box_error)
        }
    }

    /**
     * Clear error
     */
    fun clearError() {
        errorTextView.visibility = View.GONE
        updatePinBoxes()
    }

    /**
     * Request focus on PIN input
     */
    fun requestPinFocus() {
        hiddenEditText.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(hiddenEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * Clear focus
     */
    fun clearPinFocus() {
        hiddenEditText.clearFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(hiddenEditText.windowToken, 0)
    }

    /**
     * Set listener for when PIN is completely entered
     */
    fun setOnPinEnteredListener(listener: (String) -> Unit) {
        onPinEnteredListener = listener
    }

    /**
     * Set listener for PIN changes
     */
    fun setOnPinChangedListener(listener: (String, Boolean) -> Unit) {
        onPinChangedListener = listener
    }

    /**
     * Enable/disable input
     */
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        hiddenEditText.isEnabled = enabled
        alpha = if (enabled) 1f else 0.5f
    }

    /**
     * Shake animation for wrong PIN
     */
    fun shakeError() {
        val shake = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.shake)
        startAnimation(shake)

        // Also show error styling
        pinBoxes.forEach { box ->
            box.setBackgroundResource(R.drawable.bg_pin_box_error)
        }

        // Reset after animation
        postDelayed({
            updatePinBoxes()
        }, 500)
    }

    companion object {
        /**
         * Default PIN length for first-time users
         */
        const val DEFAULT_PIN_LENGTH = 10 // Phone number length

        /**
         * Minimum PIN length
         */
        const val MIN_PIN_LENGTH = 5

        /**
         * Maximum PIN length
         */
        const val MAX_PIN_LENGTH = 10
    }
}

/**
 * Extension function for easy listener setup
 */
fun PinInputView.onPinEntered(action: (String) -> Unit) {
    setOnPinEnteredListener(action)
}

/**
 * Extension function for PIN change listener
 */
fun PinInputView.onPinChanged(action: (String, Boolean) -> Unit) {
    setOnPinChangedListener(action)
}

/**
 * Extension function to verify PIN matches
 */
fun PinInputView.matchesPin(otherPin: String): Boolean {
    return getPin() == otherPin
}

/**
 * Extension function to validate and get PIN
 */
fun PinInputView.getValidatedPin(): String? {
    return if (validatePin()) {
        getPin()
    } else {
        null
    }
}

/**
 * Extension function for animated clear
 */
fun PinInputView.clearWithAnimation() {
    // Fade out animation
    animate()
        .alpha(0f)
        .setDuration(200)
        .withEndAction {
            clearPin()
            alpha = 1f
        }
        .start()
}