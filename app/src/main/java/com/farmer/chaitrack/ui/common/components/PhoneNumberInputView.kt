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
        private const val MIN_LENGTH = 10
        private const val MAX_LENGTH = 13
        private val KENYAN_PATTERN_07 = Regex("^0[17][0-9]{8}$")
        private val KENYAN_PATTERN_254 = Regex("^254[17][0-9]{8}$")
        private val KENYAN_PATTERN_PLUS = Regex("^\\+254[17][0-9]{8}$")
    }

    init {
        orientation = VERTICAL

        LayoutInflater.from(context).inflate(R.layout.view_phone_number_input, this, true)

        textInputLayout = findViewById(R.id.textInputLayout)
        editText = findViewById(R.id.editText)
        countryCodeTextView = findViewById(R.id.tvCountryCode)
        flagImageView = findViewById(R.id.ivFlag)
        errorTextView = findViewById(R.id.tvError)

        setupViews()

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.PhoneNumberInputView)

            val hint = typedArray.getString(R.styleable.PhoneNumberInputView_android_hint)
            setHint(hint ?: "Phone Number")

            val enabled = typedArray.getBoolean(R.styleable.PhoneNumberInputView_android_enabled, true)
            setEnabled(enabled)

            typedArray.recycle()
        }
    }

    private fun setupViews() {
        flagImageView.setImageResource(R.drawable.ic_flag_kenya)
        countryCodeTextView.text = countryCode

        editText.inputType = InputType.TYPE_CLASS_PHONE
        editText.filters = arrayOf(InputFilter.LengthFilter(MAX_LENGTH))
        editText.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isFormatting) return

                val input = s.toString()
                clearError()

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

    private fun formatPhoneNumber(input: String): String {
        val cleaned = input.replace(Regex("[^0-9+]"), "")

        val normalized = if (cleaned.startsWith("+")) {
            "+" + cleaned.substring(1).replace("+", "")
        } else {
            cleaned.replace("+", "")
        }

        return when {
            normalized.startsWith("+254") -> {
                val digits = normalized.substring(4)
                when {
                    digits.length <= 3 -> "+254 $digits"
                    digits.length <= 6 -> "+254 ${digits.substring(0, 3)} ${digits.substring(3)}"
                    else -> "+254 ${digits.substring(0, 3)} ${digits.substring(3, 6)} ${digits.substring(6)}"
                }
            }
            normalized.startsWith("254") -> {
                val digits = normalized.substring(3)
                when {
                    digits.length <= 3 -> "254 $digits"
                    digits.length <= 6 -> "254 ${digits.substring(0, 3)} ${digits.substring(3)}"
                    else -> "254 ${digits.substring(0, 3)} ${digits.substring(3, 6)} ${digits.substring(6)}"
                }
            }
            normalized.startsWith("0") -> {
                when {
                    normalized.length <= 4 -> normalized
                    normalized.length <= 7 -> "${normalized.substring(0, 4)} ${normalized.substring(4)}"
                    else -> "${normalized.substring(0, 4)} ${normalized.substring(4, 7)} ${normalized.substring(7)}"
                }
            }
            else -> normalized
        }
    }

    fun getNormalizedNumber(): String {
        val cleaned = editText.text.toString().replace(Regex("[^0-9]"), "")

        return when {
            cleaned.startsWith("254") -> cleaned
            cleaned.startsWith("0") -> "254${cleaned.substring(1)}"
            cleaned.startsWith("+254") -> cleaned.substring(1)
            else -> cleaned
        }
    }

    fun getLocalFormat(): String {
        val normalized = getNormalizedNumber()
        return if (normalized.startsWith("254")) {
            "0${normalized.substring(3)}"
        } else {
            normalized
        }
    }

    fun getInternationalFormat(): String {
        val normalized = getNormalizedNumber()
        return if (normalized.startsWith("254")) {
            "+$normalized"
        } else {
            normalized
        }
    }

    fun isValidPhoneNumber(): Boolean {
        val cleaned = editText.text.toString().replace(Regex("[^0-9+]"), "")

        return when {
            cleaned.matches(KENYAN_PATTERN_07) -> true
            cleaned.matches(KENYAN_PATTERN_254) -> true
            cleaned.matches(KENYAN_PATTERN_PLUS) -> true
            else -> false
        }
    }

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

    fun setPhoneNumber(phoneNumber: String) {
        editText.setText(phoneNumber)
    }

    fun getRawInput(): String {
        return editText.text.toString()
    }

    fun clearPhoneNumber() {
        editText.text?.clear()
    }

    fun setHint(hint: String) {
        textInputLayout.hint = hint
    }

    fun setHint(hintRes: Int) {
        setHint(context.getString(hintRes))
    }

    fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
        textInputLayout.error = " "
    }

    fun clearError() {
        errorTextView.visibility = View.GONE
        textInputLayout.error = null
    }

    fun isEmpty(): Boolean {
        return editText.text.toString().replace(Regex("[^0-9]"), "").isEmpty()
    }

    fun setOnPhoneNumberChangedListener(listener: (String, Boolean) -> Unit) {
        onPhoneNumberChangedListener = listener
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: android.graphics.Rect?): Boolean {
        return editText.requestFocus(direction, previouslyFocusedRect)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        editText.isEnabled = enabled
        alpha = if (enabled) 1f else 0.5f
    }

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

    enum class PhoneNumberType {
        SAFARICOM,
        AIRTEL,
        EQUITEL,
        OTHER,
        UNKNOWN
    }
}

fun PhoneNumberInputView.onPhoneNumberChanged(action: (String, Boolean) -> Unit) {
    setOnPhoneNumberChangedListener(action)
}

fun PhoneNumberInputView.getValidatedPhoneNumber(): String? {
    return if (validate()) {
        getNormalizedNumber()
    } else {
        null
    }
}

fun PhoneNumberInputView.matchesPhoneNumber(otherNumber: String): Boolean {
    return getNormalizedNumber() == otherNumber
}

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

fun PhoneNumberInputView.getMaskedNumber(): String {
    val number = getRawInput()
    return if (number.length >= 10) {
        "${number.substring(0, 3)}****${number.substring(number.length - 3)}"
    } else {
        number
    }
}

fun PhoneNumberInputView.isKenyanNumber(): Boolean {
    val normalized = getNormalizedNumber()
    return normalized.startsWith("254") && normalized.length == 12
}

fun PhoneNumberInputView.getCarrierName(): String {
    return when (getPhoneNumberType()) {
        PhoneNumberInputView.PhoneNumberType.SAFARICOM -> "Safaricom"
        PhoneNumberInputView.PhoneNumberType.AIRTEL -> "Airtel"
        PhoneNumberInputView.PhoneNumberType.EQUITEL -> "Equitel"
        PhoneNumberInputView.PhoneNumberType.OTHER -> "Other"
        PhoneNumberInputView.PhoneNumberType.UNKNOWN -> "Unknown"
    }
}