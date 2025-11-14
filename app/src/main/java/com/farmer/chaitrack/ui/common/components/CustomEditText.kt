package com.farmer.chaitrack.ui.common.components

import android.content.Context
import android.graphics.drawable.Drawable
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
 * Custom EditText component with validation, icons, and error handling
 * Wraps Material TextInputLayout with additional features
 */
class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val textInputLayout: TextInputLayout
    private val editText: EditText
    private val iconImageView: ImageView
    private val errorTextView: TextView

    private var validationRules = mutableListOf<ValidationRule>()
    private var onTextChangedListener: ((String) -> Unit)? = null


    init {
        orientation = VERTICAL

        LayoutInflater.from(context).inflate(R.layout.view_custom_edit_text, this, true)

        textInputLayout = findViewById(R.id.textInputLayout)
        editText = findViewById(R.id.editText)
        iconImageView = findViewById(R.id.ivIcon)
        errorTextView = findViewById(R.id.tvError)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomEditText)

            // System attributes
            val hint = typedArray.getString(R.styleable.CustomEditText_android_hint)
            setHint(hint ?: "")

            // Input type
            val inputStyleIndex = typedArray.getInt(R.styleable.CustomEditText_inputStyle, 0)
            val inputStyle = InputStyle.values()[inputStyleIndex]
            setInputStyle(inputStyle)

            // Icon
            val iconDrawable = typedArray.getDrawable(R.styleable.CustomEditText_startIcon)
            if (iconDrawable != null) {
                setStartIcon(iconDrawable)
            } else {
                iconImageView.visibility = GONE
            }

            // Max length
            val maxLength = typedArray.getInt(R.styleable.CustomEditText_android_maxLength, -1)
            if (maxLength > 0) {
                setMaxLength(maxLength)
            }

            // Enabled state
            val isEnabled = typedArray.getBoolean(R.styleable.CustomEditText_android_enabled, true)
            setEnabled(isEnabled)

            // Text
            val text = typedArray.getString(R.styleable.CustomEditText_android_text)
            text?.let { setText(it) }

            // Text color
            val textColor = typedArray.getColor(R.styleable.CustomEditText_android_textColor, -1)
            if (textColor != -1) {
                editText.setTextColor(textColor)
            }

            // Text size
            val textSize = typedArray.getDimension(R.styleable.CustomEditText_android_textSize, -1f)
            if (textSize != -1f) {
                editText.textSize = textSize / resources.displayMetrics.density
            }

            typedArray.recycle()
        }

        // Setup text change listener
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Clear error when user types
                clearError()
                onTextChangedListener?.invoke(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
    /**
     * Input types for the EditText
     */
    enum class InputStyle {
        TEXT,
        NUMBER,
        PHONE,
        PIN,
        EMAIL,
        MULTILINE
    }

    /**
     * Validation rule interface
     */
    interface ValidationRule {
        fun validate(text: String): ValidationResult
    }

    /**
     * Validation result
     */
    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )

    init {
        orientation = VERTICAL

        // Inflate layout
        LayoutInflater.from(context).inflate(R.layout.view_custom_edit_text, this, true)

        // Load custom attributes
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomEditText)

            // Hint
            val hint = typedArray.getString(R.styleable.CustomEditText_android_hint)
            setHint(hint ?: "")

            // Input type
            val inputStyleIndex = typedArray.getInt(R.styleable.CustomEditText_inputStyle, 0)
            val inputStyle = InputStyle.values()[inputStyleIndex]
            setInputStyle(inputStyle)

            // Icon
            val iconDrawable = typedArray.getDrawable(R.styleable.CustomEditText_startIcon)
            if (iconDrawable != null) {
                setStartIcon(iconDrawable)
            } else {
                iconImageView.visibility = GONE
            }

            // Max length
            val maxLength = typedArray.getInt(R.styleable.CustomEditText_android_maxLength, -1)
            if (maxLength > 0) {
                setMaxLength(maxLength)
            }

            // Enabled state
            val isEnabled = typedArray.getBoolean(R.styleable.CustomEditText_android_enabled, true)
            setEnabled(isEnabled)

            typedArray.recycle()
        }

        // Setup text change listener
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Clear error when user types
                clearError()
                onTextChangedListener?.invoke(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * Set hint text
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
     * Get text
     */
    fun getText(): String {
        return editText.text.toString()
    }

    /**
     * Get trimmed text
     */
    fun getTrimmedText(): String {
        return getText().trim()
    }

    /**
     * Set text
     */
    fun setText(text: String) {
        editText.setText(text)
    }

    /**
     * Set input style
     */
    fun setInputStyle(style: InputStyle) {
        val inputType = when (style) {
            InputStyle.TEXT -> InputType.TYPE_CLASS_TEXT
            InputStyle.NUMBER -> InputType.TYPE_CLASS_NUMBER
            InputStyle.PHONE -> InputType.TYPE_CLASS_PHONE
            InputStyle.PIN -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            InputStyle.EMAIL -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            InputStyle.MULTILINE -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        }
        editText.inputType = inputType

        // Set max lines for multiline
        if (style == InputStyle.MULTILINE) {
            editText.maxLines = 5
            editText.minLines = 3
        }
    }

    /**
     * Set start icon
     */
    fun setStartIcon(drawable: Drawable?) {
        if (drawable != null) {
            iconImageView.visibility = VISIBLE
            iconImageView.setImageDrawable(drawable)
        } else {
            iconImageView.visibility = GONE
        }
    }

    /**
     * Set start icon from resource
     */
    fun setStartIcon(iconRes: Int) {
        setStartIcon(ContextCompat.getDrawable(context, iconRes))
    }

    /**
     * Set max length
     */
    fun setMaxLength(maxLength: Int) {
        editText.filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }

    /**
     * Show error message
     */
    fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = VISIBLE
        textInputLayout.error = " "
    }

    /**
     * Clear error
     */
    fun clearError() {
        errorTextView.visibility = GONE
        textInputLayout.error = null
    }

    /**
     * Check if field is empty
     */
    fun isEmpty(): Boolean {
        return getTrimmedText().isEmpty()
    }

    /**
     * Check if field is not empty
     */
    fun isNotEmpty(): Boolean {
        return getTrimmedText().isNotEmpty()
    }

    /**
     * Add validation rule
     */
    fun addValidationRule(rule: ValidationRule) {
        validationRules.add(rule)
    }

    /**
     * Add validation rule with lambda
     */
    fun addValidationRule(errorMessage: String, validator: (String) -> Boolean) {
        validationRules.add(object : ValidationRule {
            override fun validate(text: String): ValidationResult {
                return if (validator(text)) {
                    ValidationResult(true)
                } else {
                    ValidationResult(false, errorMessage)
                }
            }
        })
    }

    /**
     * Validate input against all rules
     */
    fun validate(): Boolean {
        val text = getTrimmedText()

        for (rule in validationRules) {
            val result = rule.validate(text)
            if (!result.isValid) {
                showError(result.errorMessage ?: "Invalid input")
                return false
            }
        }

        clearError()
        return true
    }

    /**
     * Set text change listener
     */
    fun setOnTextChangedListener(listener: (String) -> Unit) {
        onTextChangedListener = listener
    }

    /**
     * Set focus change listener
     */
    fun setOnFocusChangeListener(listener: (Boolean) -> Unit) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            listener(hasFocus)
        }
    }

    /**
     * Request focus on the EditText
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

    companion object {
        /**
         * Common validation rules
         */
        object ValidationRules {
            /**
             * Required field validation
             */
            fun required(errorMessage: String = "This field is required"): ValidationRule {
                return object : ValidationRule {
                    override fun validate(text: String): ValidationResult {
                        return ValidationResult(
                            text.trim().isNotEmpty(),
                            errorMessage
                        )
                    }
                }
            }

            /**
             * Phone number validation (10-13 digits)
             */
            fun phoneNumber(errorMessage: String = "Invalid phone number"): ValidationRule {
                return object : ValidationRule {
                    override fun validate(text: String): ValidationResult {
                        val isValid = text.matches(Regex("^[0-9]{10,13}$"))
                        return ValidationResult(isValid, errorMessage)
                    }
                }
            }

            /**
             * PIN validation (5-10 digits)
             */
            fun pin(errorMessage: String = "PIN must be 5-10 digits"): ValidationRule {
                return object : ValidationRule {
                    override fun validate(text: String): ValidationResult {
                        val isValid = text.matches(Regex("^[0-9]{5,10}$"))
                        return ValidationResult(isValid, errorMessage)
                    }
                }
            }

            /**
             * Email validation
             */
            fun email(errorMessage: String = "Invalid email address"): ValidationRule {
                return object : ValidationRule {
                    override fun validate(text: String): ValidationResult {
                        val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
                        return ValidationResult(isValid, errorMessage)
                    }
                }
            }

            /**
             * Minimum length validation
             */
            fun minLength(
                length: Int,
                errorMessage: String = "Must be at least $length characters"
            ): ValidationRule {
                return object : ValidationRule {
                    override fun validate(text: String): ValidationResult {
                        return ValidationResult(
                            text.length >= length,
                            errorMessage
                        )
                    }
                }
            }

            /**
             * Maximum length validation
             */
            fun maxLength(
                length: Int,
                errorMessage: String = "Must not exceed $length characters"
            ): ValidationRule {
                return object : ValidationRule {
                    override fun validate(text: String): ValidationResult {
                        return ValidationResult(
                            text.length <= length,
                            errorMessage
                        )
                    }
                }
            }

            /**
             * Numeric validation
             */
            fun numeric(errorMessage: String = "Must contain only numbers"): ValidationRule {
                return object : ValidationRule {
                    override fun validate(text: String): ValidationResult {
                        val isValid = text.matches(Regex("^[0-9]+$"))
                        return ValidationResult(isValid, errorMessage)
                    }
                }
            }

            /**
             * Match validation (e.g., for confirm password)
             */
            fun matches(
                otherValue: String,
                errorMessage: String = "Values do not match"
            ): ValidationRule {
                return object : ValidationRule {
                    override fun validate(text: String): ValidationResult {
                        return ValidationResult(
                            text == otherValue,
                            errorMessage
                        )
                    }
                }
            }

            /**
             * Range validation for numbers
             */
            fun numberRange(
                min: Int,
                max: Int,
                errorMessage: String = "Must be between $min and $max"
            ): ValidationRule {
                return object : ValidationRule {
                    override fun validate(text: String): ValidationResult {
                        val number = text.toIntOrNull()
                        val isValid = number != null && number in min..max
                        return ValidationResult(isValid, errorMessage)
                    }
                }
            }
        }
    }
}

/**
 * Extension function to validate multiple CustomEditTexts
 */
fun List<CustomEditText>.validateAll(): Boolean {
    var allValid = true
    forEach { editText ->
        if (!editText.validate()) {
            allValid = false
        }
    }
    return allValid
}

/**
 * Extension function to clear all errors
 */
fun List<CustomEditText>.clearAllErrors() {
    forEach { it.clearError() }
}

/**
 * Extension function to get all text values
 */
fun List<CustomEditText>.getAllText(): List<String> {
    return map { it.getTrimmedText() }
}

/**
 * Extension function for easier validation setup - Fixed version
 */
fun CustomEditText.validateRequired(errorMessage: String = "This field is required"): CustomEditText {
    this.addValidationRule(errorMessage) { text -> text.trim().isNotEmpty() }
    return this
}

fun CustomEditText.validatePhoneNumber(errorMessage: String = "Invalid phone number"): CustomEditText {
    this.addValidationRule(errorMessage) { text -> text.matches(Regex("^[0-9]{10,13}$")) }
    return this
}

fun CustomEditText.validatePin(errorMessage: String = "PIN must be 5-10 digits"): CustomEditText {
    this.addValidationRule(errorMessage) { text -> text.matches(Regex("^[0-9]{5,10}$")) }
    return this
}

fun CustomEditText.validateEmail(errorMessage: String = "Invalid email address"): CustomEditText {
    this.addValidationRule(errorMessage) { text -> android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches() }
    return this
}

fun CustomEditText.validateMinLength(length: Int, errorMessage: String? = null): CustomEditText {
    this.addValidationRule(errorMessage ?: "Must be at least $length characters") { text ->
        text.length >= length
    }
    return this
}

fun CustomEditText.validateMaxLength(length: Int, errorMessage: String? = null): CustomEditText {
    this.addValidationRule(errorMessage ?: "Must not exceed $length characters") { text ->
        text.length <= length
    }
    return this
}

fun CustomEditText.validateNumeric(errorMessage: String = "Must contain only numbers"): CustomEditText {
    this.addValidationRule(errorMessage) { text -> text.matches(Regex("^[0-9]+$")) }
    return this
}

fun CustomEditText.validateMatches(otherValue: String, errorMessage: String = "Values do not match"): CustomEditText {
    this.addValidationRule(errorMessage) { text -> text == otherValue }
    return this
}

fun CustomEditText.validateNumberRange(min: Int, max: Int, errorMessage: String = "Must be between $min and $max"): CustomEditText {
    this.addValidationRule(errorMessage) { text ->
        val number = text.toIntOrNull()
        number != null && number in min..max
    }
    return this
}

/**
 * DSL for text change handling
 */
fun CustomEditText.onTextChanged(action: (String) -> Unit) {
    setOnTextChangedListener(action)
}