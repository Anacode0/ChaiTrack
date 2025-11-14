package com.farmer.chaitrack.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import com.farmer.chaitrack.domain.model.DeliveryItem
import com.farmer.chaitrack.data.local.entity.DeliveryEntity
import com.farmer.chaitrack.data.remote.dto.DeliveryDto

// Context Extensions

/**
 * Show a short toast message
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

// Extension function to convert DeliveryEntity to DeliveryItem
fun DeliveryEntity.toDomainModel(): DeliveryItem {
    return DeliveryItem(
        id = this.id,
        userId = this.userId,
        clerkName = this.clerkName,
        weight = this.weight,
        date = this.date,
        time = this.time,
        monthTotal = this.monthTotal,
        status = this.status,
        pricePerKg = this.pricePerKg,
        totalAmount = this.totalAmount,
        collectionCenter = this.collectionCenter
    )
}

// Extension function to convert DeliveryDto to DeliveryEntity
fun DeliveryDto.toEntity(): DeliveryEntity {
    return DeliveryEntity(
        id = this.id,
        userId = this.userId,
        clerkName = this.clerkName,
        weight = this.weight,
        date = this.date,
        time = this.time,
        monthTotal = this.totalAmount,
        status = this.status,
        pricePerKg = this.pricePerKg,
        totalAmount = this.totalAmount,
        collectionCenter = this.collectionCenter
    )
}

/**
 * Show a long toast message
 */
fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * Show toast with string resource
 */
fun Context.showToast(@StringRes messageRes: Int) {
    Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
}

/**
 * Hide keyboard from current focus
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Show keyboard on a view
 */
fun Context.showKeyboard(view: View) {
    view.requestFocus()
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Make a phone call
 */
fun Context.makePhoneCall(phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    } catch (e: Exception) {
        Timber.e(e, "Error making phone call")
        showToast("Unable to make phone call")
    }
}

/**
 * Share text content
 */
fun Context.shareText(text: String, title: String = "Share via") {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(intent, title))
    } catch (e: Exception) {
        Timber.e(e, "Error sharing text")
        showToast("Unable to share")
    }
}

/**
 * Share file (e.g., PDF)
 */
fun Context.shareFile(uri: Uri, mimeType: String = "*/*", title: String = "Share file") {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, title))
    } catch (e: Exception) {
        Timber.e(e, "Error sharing file")
        showToast("Unable to share file")
    }
}

// ==================== View Extensions ====================

/**
 * Show view
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Hide view
 */
fun View.hide() {
    visibility = View.GONE
}

/**
 * Make view invisible
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Toggle view visibility
 */
fun View.toggleVisibility() {
    visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
}

/**
 * Show view with condition
 */
fun View.showIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}

/**
 * Enable or disable view
 */
fun View.setEnabled(enabled: Boolean, alpha: Float = if (enabled) 1f else 0.5f) {
    isEnabled = enabled
    this.alpha = alpha
}

/**
 * Show snackbar
 */
fun View.showSnackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionText: String? = null,
    action: (() -> Unit)? = null
) {
    val snackbar = Snackbar.make(this, message, duration)
    if (actionText != null && action != null) {
        snackbar.setAction(actionText) { action() }
    }
    snackbar.show()
}

// ==================== EditText Extensions ====================

/**
 * Get trimmed text from EditText
 */
fun EditText.getTrimmedText(): String = text.toString().trim()

/**
 * Check if EditText is empty
 */
fun EditText.isEmpty(): Boolean = getTrimmedText().isEmpty()

/**
 * Check if EditText is not empty
 */
fun EditText.isNotEmpty(): Boolean = getTrimmedText().isNotEmpty()

/**
 * Set text and move cursor to end
 */
fun EditText.setTextAndMoveCursor(text: String) {
    setText(text)
    setSelection(text.length)
}

/**
 * Add text changed listener with simplified callbacks
 */
fun EditText.onTextChanged(
    afterTextChanged: ((String) -> Unit)? = null,
    beforeTextChanged: ((String) -> Unit)? = null,
    onTextChanged: ((String) -> Unit)? = null
): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged?.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged?.invoke(s.toString())
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged?.invoke(s.toString())
        }
    }
    addTextChangedListener(textWatcher)
    return textWatcher
}

/**
 * Validate phone number format
 */
fun EditText.isValidPhoneNumber(): Boolean {
    val phoneNumber = getTrimmedText()
    return phoneNumber.matches(Regex("^[0-9]{10,13}$"))
}

/**
 * Validate PIN format (5-10 digits)
 */
fun EditText.isValidPin(): Boolean {
    val pin = getTrimmedText()
    return pin.matches(Regex("^[0-9]{5,10}$"))
}

// ==================== String Extensions ====================

/**
 * Check if string is a valid phone number
 */
fun String.isValidPhoneNumber(): Boolean {
    return matches(Regex("^[0-9]{10,13}$"))
}

/**
 * Check if string is a valid PIN
 */
fun String.isValidPin(): Boolean {
    return matches(Regex("^[0-9]{5,10}$"))
}

/**
 * Mask phone number (e.g., 0712345678 -> 071****678)
 */
fun String.maskPhoneNumber(): String {
    return if (length >= 10) {
        "${substring(0, 3)}****${substring(length - 3)}"
    } else {
        this
    }
}

/**
 * Mask PIN (e.g., 123456 -> ******)
 */
fun String.maskPin(): String {
    return "*".repeat(length)
}

/**
 * Capitalize first letter of each word
 */
fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { word ->
        word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}

// ==================== Number Extensions ====================

/**
 * Format double as currency (KES)
 */
fun Double.formatAsCurrency(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "KE"))
    formatter.currency = Currency.getInstance("KES")
    return formatter.format(this)
}

/**
 * Format double with 2 decimal places
 */
fun Double.formatDecimal(): String {
    val formatter = DecimalFormat("#,##0.00")
    return formatter.format(this)
}

/**
 * Format as weight in kg
 */
fun Double.formatAsWeight(): String {
    return "${formatDecimal()} kg"
}

/**
 * Format int as currency (KES)
 */
fun Int.formatAsCurrency(): String {
    return toDouble().formatAsCurrency()
}

/**
 * Format float as weight in kg
 */
fun Float.formatAsWeight(): String {
    return toDouble().formatAsWeight()
}

// ==================== Date Extensions ====================

/**
 * Format date to string with pattern
 */
fun Date.format(pattern: String = "dd MMM yyyy"): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this)
}

/**
 * Format date to time string
 */
fun Date.formatTime(pattern: String = "HH:mm"): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this)
}

/**
 * Format date to datetime string
 */
fun Date.formatDateTime(pattern: String = "dd MMM yyyy, HH:mm"): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this)
}

/**
 * Check if date is today
 */
fun Date.isToday(): Boolean {
    val today = Calendar.getInstance()
    val dateCalendar = Calendar.getInstance().apply { time = this@isToday }

    return today.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == dateCalendar.get(Calendar.DAY_OF_YEAR)
}

/**
 * Get relative time string (e.g., "2 hours ago", "Yesterday")
 */
fun Date.toRelativeTime(): String {
    val now = System.currentTimeMillis()
    val time = this.time
    val diff = now - time

    return when {
        diff < 60_000 -> "Just now"
        diff < 3600_000 -> "${diff / 60_000} minutes ago"
        diff < 7200_000 -> "1 hour ago"
        diff < 86400_000 -> "${diff / 3600_000} hours ago"
        isToday() -> "Today at ${formatTime()}"
        diff < 172800_000 -> "Yesterday at ${formatTime()}"
        diff < 604800_000 -> "${diff / 86400_000} days ago"
        else -> format("dd MMM yyyy")
    }
}

// ==================== Long Extensions ====================

/**
 * Convert timestamp to Date
 */
fun Long.toDate(): Date = Date(this)

/**
 * Format timestamp as date string
 */
fun Long.formatAsDate(pattern: String = "dd MMM yyyy"): String {
    return toDate().format(pattern)
}

/**
 * Format timestamp as time string
 */
fun Long.formatAsTime(pattern: String = "HH:mm"): String {
    return toDate().formatTime(pattern)
}

/**
 * Format timestamp as datetime string
 */
fun Long.formatAsDateTime(pattern: String = "dd MMM yyyy, HH:mm"): String {
    return toDate().formatDateTime(pattern)
}

// ==================== LiveData Extensions ====================

/**
 * Observe LiveData with simplified syntax
 */
fun <T> LiveData<T>.observe(owner: LifecycleOwner, action: (T) -> Unit) {
    observe(owner, Observer { action(it) })
}

/**
 * Observe LiveData once
 */
fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, action: (T) -> Unit) {
    observe(owner, object : Observer<T> {
        override fun onChanged(value: T) {
            action(value)
            removeObserver(this)
        }
    })
}

// ==================== Activity Extensions ====================

/**
 * Start activity with extras
 */
inline fun <reified T : Activity> Activity.startActivity(
    extras: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    intent.extras()
    startActivity(intent)
}

/**
 * Start activity and finish current
 */
inline fun <reified T : Activity> Activity.startActivityAndFinish(
    extras: Intent.() -> Unit = {}
) {
    startActivity<T>(extras)
    finish()
}

/**
 * Hide keyboard for activity
 */
fun Activity.hideKeyboard() {
    currentFocus?.let { view ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

// ==================== Fragment Extensions ====================

/**
 * Hide keyboard for fragment
 */
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard() }
}

// ==================== Collection Extensions ====================

/**
 * Safe get item from list
 */
fun <T> List<T>.getOrNull(index: Int): T? {
    return if (index in indices) get(index) else null
}

/**
 * Calculate sum of doubles
 */
fun List<Double>.sum(): Double {
    return fold(0.0) { acc, value -> acc + value }
}

/**
 * Group by date
 */
fun <T> List<T>.groupByDate(dateSelector: (T) -> Date): Map<String, List<T>> {
    return groupBy { dateSelector(it).format("dd MMM yyyy") }
}

// ==================== Boolean Extensions ====================

/**
 * Convert boolean to int (1 or 0)
 */
fun Boolean.toInt(): Int = if (this) 1 else 0

/**
 * Convert boolean to visibility
 */
fun Boolean.toVisibility(): Int = if (this) View.VISIBLE else View.GONE

// ==================== Utility Functions ====================

/**
 * Run block and catch exceptions
 */
inline fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (e: Exception) {
        Timber.e(e, "Error in tryOrNull")
        null
    }
}

/**
 * Run block and return default on exception
 */
inline fun <T> tryOrDefault(default: T, block: () -> T): T {
    return try {
        block()
    } catch (e: Exception) {
        Timber.e(e, "Error in tryOrDefault")
        default
    }
}