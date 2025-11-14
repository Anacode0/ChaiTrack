package com.farmer.chaitrack.ui.common.components

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.farmer.chaitrack.R
import com.google.android.material.textfield.TextInputLayout

/**
 * Supports both 07XX XXX XXX and 254XXX XXX XXX formats
 */
class PhoneNumberInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val textInputLayout: TextInputLayout
    private val editText: EditText
    private val countryCodeTextView: TextView
    private val flagImageView: ImageView
    private val errorTextView: TextView

    private var countryCode: String = "+254"
    private var formattedNumber: String = ""
    private var onPhoneNumberChangedListener: ((String, Boolean) -> Unit)? = null

    companion object {
        // Kenyan phone number patterns
        private const val MIN_LENGTH = 10 // 0712345678
        private const val MAX_LENGTH = 13 // 254712345678 or +254712345678

        // Formatting patterns
        private val KENYAN_PATTERN_07 = Regex("^0[17][0-9]{8}$")
        private val KENYAN_PATTERN_254 = Regex("^254[17][0-9]{8}$")
        private val KENYAN_PATTERN_PLUS = Regex("^\\+254[17][0-9]{8}$")
    }

    init {
        orientation = VERTICAL

        // Inflate layout
        LayoutInflater.from(context).inflate(R.layout.view_phone_number_input, this, true)

        // Initialize views
        textInputLayout = findViewById(R.id.textInputLayout)
        editText = findViewById(R.id.editText)
        countryCodeTextView = findViewById(R.id.tvCountryCode)
        flagImageView = findViewById(R.id.ivFlag)
        errorTextView = findViewById(R.id.tvError)

        // Setup views
        setupViews()

        // Load custom attributes
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.PhoneNumberInputView)

            val hint = typedArray.getString(R.styleable.PhoneNumberInputView_android_hint)
            setHint(hint ?: "Phone Number")

            val enabled = typedArray.getBoolean(R.styleable.PhoneNumberInputView_android_enabled, true)
            setEnabled(enabled)

            typedArray.recycle()
        }
    }

    /**
     * Setup views
     */
    private fun setupViews() {
        // Set Kenya flag
        flagImageView.setImageResource(R.drawable.ic_flag_kenya)
        countryCodeTextView.text = countryCode

        // Configure EditText
        editText.inputType = InputType.TYPE_CLASS_PHONE
        editText.filters = arrayOf(InputFilter.LengthFilter(MAX_LENGTH))

        // Add text watcher
        editText.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isFormatting) return

                val input = s.toString()
                clearError()

                // Format the number
                isFormatting = true
                val formatted = formatPhoneNumber(input)
                if (formatted != input) {
                    editText.setText(formatted)
                    editText.setSelection(formatted.length)
                }
                isFormatting = false

                formattedNumber = getNormalizedNumber()
                val isValid = isValidPhoneNumber()
                onPhoneNumberChangedListener?.invoke(formattedNumber, isValid)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * Format phone number as user types
     */
    private fun formatPhoneNumber(input: String): String {
        // Remove all non-digit characters except +
        val cleaned = input.replace(Regex("[^0-9+]"), "")

        // Remove + if it's not at the start
        val normalized = if (cleaned.startsWith("+")) {
            "+" + cleaned.substring(1).replace("+", "")
        } else {
            cleaned.replace("+", "")
        }

        return when {
            // Starting with +254
            normalized.startsWith("+254") -> {
                val digits = normalized.substring(4)
                when {
                    digits.length <= 3 -> "+254 $digits"
                    digits.length <= 6 -> "+254 ${digits.substring(0, 3)} ${digits.substring(3)}"
                    else -> "+254 ${digits.substring(0, 3)} ${digits.substring(3, 6)} ${digits.substring(6)}"
                }
            }
            // Starting with 254
            normalized.startsWith("254") -> {
                val digits = normalized.substring(3)
                when {
                    digits.length <= 3 -> "254 $digits"
                    digits.length <= 6 -> "254 ${digits.substring(0, 3)} ${digits.substring(3)}"
                    else -> "254 ${digits.substring(0, 3)} ${digits.substring(3, 6)} ${digits.substring(6)}"
                }
            }
            // Starting with 0 (local format)
            normalized.startsWith("0") -> {
                when {
                    normalized.length <= 4 -> normalized
                    normalized.length <= 7 -> "${normalized.substring(0, 4)} ${normalized.substring(4)}"
                    else -> "${normalized.substring(0, 4)} ${normalized.substring(4, 7)} ${normalized.substring(7)}"
                }
            }
            // Just digits
            else -> normalized
        }
    }

    /**
     * Get normalized phone number (254XXXXXXXXX format)
     */
    fun getNormalizedNumber(): String {
        val cleaned = editText.text.toString().replace(Regex("[^0-9]"), "")

        return when {
            cleaned.startsWith("254") -> cleaned
            cleaned.startsWith("0") -> "254${cleaned.substring(1)}"
            cleaned.startsWith("+254") -> cleaned.substring(1)
            else -> cleaned
        }
    }

    /**
     * Get phone number in local format (07XXXXXXXX)
     */
    fun getLocalFormat(): String {
        val normalized = getNormalizedNumber()
        return if (normalized.startsWith("254")) {
            "0${normalized.substring(3)}"
        } else {
            normalized
        }
    }

    /**
     * Get phone number in international format (+254XXXXXXXXX)
     */
    fun getInternationalFormat(): String {
        val normalized = getNormalizedNumber()
        return if (normalized.startsWith("254")) {
            "+$normalized"
        } else {
            normalized
        }
    }

    /**
     * Validate phone number
     */
    fun isValidPhoneNumber(): Boolean {
        val cleaned = editText.text.toString().replace(Regex("[^0-9+]"), "")

        return when {
            cleaned.matches(KENYAN_PATTERN_07) -> true
            cleaned.matches(KENYAN_PATTERN_254) -> true
            cleaned.matches(KENYAN_PATTERN_PLUS) -> true
            else -> false
        }
    }

    /**
     * Validate with error display
     */
    fun validate(): Boolean {
        val input = editText.text.toString()

        return when {
            input.isBlank() -> {
                showError("Phone number is required")
                false
            }
            !isValidPhoneNumber() -> {
                showError("Invalid phone number format")
                false
            }
            else -> {
                clearError()
                true
            }
        }
    }

    /**
     * Set phone number
     */
    fun setPhoneNumber(phoneNumber: String) {
        editText.setText(phoneNumber)
    }

    /**
     * Get raw input
     */
    fun getRawInput(): String {
        return editText.text.toString()
    }

    /**
     * Clear phone number
     */
    fun clearPhoneNumber() {
        editText.text?.clear()
    }

    /**
     * Set hint
     */
    fun setHint(hint: String) {
        textInputLayout.hint = hint
    }

    /**
     * Set hint from resource
     */
    fun setHint(hintRes: Int) {
        setHint(context.getString(hintRes))
    }

    /**
     * Show error message
     */
    fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
        textInputLayout.error = " " // Space to show error state
    }

    /**
     * Clear error
     */
    fun clearError() {
        errorTextView.visibility = View.GONE
        textInputLayout.error = null
    }

    /**
     * Check if empty
     */
    fun isEmpty(): Boolean {
        return editText.text.toString().replace(Regex("[^0-9]"), "").isEmpty()
    }

    /**
     * Set phone number changed listener
     */
    fun setOnPhoneNumberChangedListener(listener: (String, Boolean) -> Unit) {
        onPhoneNumberChangedListener = listener
    }

    /**
     * Request focus
     */
    override fun requestFocus(direction: Int, previouslyFocusedRect: android.graphics.Rect?): Boolean {
        return editText.requestFocus(direction, previouslyFocusedRect)
    }

    /**
     * Set enabled state
     */
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        editText.isEnabled = enabled
        alpha = if (enabled) 1f else 0.5f
    }

    /**
     * Get phone number type (Safaricom, Airtel, etc.)
     */
    fun getPhoneNumberType(): PhoneNumberType {
        val normalized = getNormalizedNumber()

        return when {
            normalized.isEmpty() -> PhoneNumberType.UNKNOWN
            normalized.startsWith("2547") || normalized.startsWith("07") -> PhoneNumberType.SAFARICOM
            normalized.startsWith("2541") || normalized.startsWith("01") -> PhoneNumberType.SAFARICOM
            normalized.startsWith("2547") && normalized[4] in listOf('3', '8') -> PhoneNumberType.AIRTEL
            normalized.startsWith("2547") && normalized[4] == '6' -> PhoneNumberType.EQUITEL
            else -> PhoneNumberType.OTHER
        }
    }

    /**
     * Phone number type enum
     */
    enum class PhoneNumberType {
        SAFARICOM,
        AIRTEL,
        EQUITEL,
        OTHER,
        UNKNOWN
    }
}

/**
 * Extension function for easy listener setup
 */
fun PhoneNumberInputView.onPhoneNumberChanged(action: (String, Boolean) -> Unit) {
    setOnPhoneNumberChangedListener(action)
}

/**
 * Extension function to get validated phone number
 */
fun PhoneNumberInputView.getValidatedPhoneNumber(): String? {
    return if (validate()) {
        getNormalizedNumber()
    } else {
        null
    }
}

/**
 * Extension function to check if phone number matches
 */
fun PhoneNumberInputView.matchesPhoneNumber(otherNumber: String): Boolean {
    return getNormalizedNumber() == otherNumber
}

/**
 * Extension function to format and set phone number
 */
fun PhoneNumberInputView.setAndFormatPhoneNumber(phoneNumber: String) {
    // Remove all non-digits
    val cleaned = phoneNumber.replace(Regex("[^0-9]"), "")

    // Normalize to 254 format
    val normalized = when {
        cleaned.startsWith("254") -> cleaned
        cleaned.startsWith("0") -> "254${cleaned.substring(1)}"
        else -> cleaned
    }

    setPhoneNumber(normalized)
}

/**
 * Extension function to display phone number in masked format
 */
fun PhoneNumberInputView.getMaskedNumber(): String {
    val number = getRawInput()
    return if (number.length >= 10) {
        "${number.substring(0, 3)}****${number.substring(number.length - 3)}"
    } else {
        number
    }
}

/**
 * Extension function to check if it's a Kenyan number
 */
fun PhoneNumberInputView.isKenyanNumber(): Boolean {
    val normalized = getNormalizedNumber()
    return normalized.startsWith("254") && normalized.length == 12
}

/**
 * Extension function to get carrier info
 */
fun PhoneNumberInputView.getCarrierName(): String {
    return when (getPhoneNumberType()) {
        PhoneNumberInputView.PhoneNumberType.SAFARICOM -> "Safaricom"
        PhoneNumberInputView.PhoneNumberType.AIRTEL -> "Airtel"
        PhoneNumberInputView.PhoneNumberType.EQUITEL -> "Equitel"
        PhoneNumberInputView.PhoneNumberType.OTHER -> "Other"
        PhoneNumberInputView.PhoneNumberType.UNKNOWN -> "Unknown"
    }
}