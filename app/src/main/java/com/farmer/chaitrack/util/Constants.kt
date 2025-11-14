package com.farmer.chaitrack.util

/**
 * Application-wide constants
 */
object Constants {

    // ==================== Network Constants ====================

    /**
     * Base URL for API requests
     * TODO: Replace with actual production URL
     */
    const val BASE_URL = "https://api.chaitrack.com/v1/"

    /**
     * Connection timeout in seconds
     */
    const val CONNECT_TIMEOUT = 30L

    /**
     * Read timeout in seconds
     */
    const val READ_TIMEOUT = 30L

    /**
     * Write timeout in seconds
     */
    const val WRITE_TIMEOUT = 30L

    // ==================== API Endpoints ====================

    object Endpoints {
        const val LOGIN = "auth/login"
        const val LOGOUT = "auth/logout"
        const val CHANGE_PIN = "auth/change-pin"
        const val FORGOT_PIN = "auth/forgot-pin"
        const val REFRESH_TOKEN = "auth/refresh"

        const val DELIVERIES = "deliveries"
        const val DELIVERY_BY_ID = "deliveries/{id}"
        const val CREATE_DELIVERY = "deliveries/create"
        const val REPORT_DELIVERY_ISSUE = "deliveries/report-issue"

        const val FERTILIZER_APPLICATIONS = "fertilizer/applications"
        const val FERTILIZER_APPLICATION_BY_ID = "fertilizer/applications/{id}"
        const val APPLY_FERTILIZER = "fertilizer/apply"
        const val FERTILIZER_STATUS = "fertilizer/status"

        const val FINANCE_RECORDS = "finance/records"
        const val FINANCE_RECORD_BY_ID = "finance/records/{id}"
        const val GENERATE_TRANSCRIPT = "finance/generate-transcript"
        const val DOWNLOAD_TRANSCRIPT = "finance/download/{id}"

        const val NOTIFICATIONS = "notifications"
        const val MARK_NOTIFICATION_READ = "notifications/{id}/read"
        const val MARK_ALL_READ = "notifications/mark-all-read"
        const val DELETE_NOTIFICATION = "notifications/{id}"

        const val ANNOUNCEMENTS = "announcements"
        const val ANNOUNCEMENT_BY_ID = "announcements/{id}"

        const val SUPPORT_CHAT = "support/messages"
        const val SEND_MESSAGE = "support/send"
        const val UPLOAD_ATTACHMENT = "support/upload"

        const val USER_PROFILE = "user/profile"
        const val UPDATE_PROFILE = "user/update"
        const val UPLOAD_PROFILE_IMAGE = "user/upload-image"
    }

    // ==================== Shared Preferences Keys ====================

    object PrefsKeys {
        const val AUTH_TOKEN = "auth_token"
        const val USER_ID = "user_id"
        const val USER_PHONE = "user_phone"
        const val USER_NAME = "user_name"
        const val USER_EMAIL = "user_email"
        const val USER_PROFILE_IMAGE = "user_profile_image"
        const val USER_FARMER_CODE = "user_farmer_code"
        const val USER_PLOT_SIZE = "user_plot_size"
        const val IS_LOGGED_IN = "is_logged_in"
        const val FIRST_LOGIN_DATE = "first_login_date"
        const val LAST_LOGIN_DATE = "last_login_date"
        const val TOKEN_EXPIRY = "token_expiry"
        const val REFRESH_TOKEN = "refresh_token"
        const val CURRENT_PRICE_PER_KG = "current_price_per_kg"
        const val LAST_SYNC_TIME = "last_sync_time"
    }

    // ==================== Database Constants ====================

    object Database {
        const val NAME = "chaitrack_database"
        const val VERSION = 1

        // Table names
        const val DELIVERIES_TABLE = "deliveries"
        const val FERTILIZER_TABLE = "fertilizer_applications"
        const val FINANCE_TABLE = "finance_records"
        const val NOTIFICATIONS_TABLE = "notifications"
        const val ANNOUNCEMENTS_TABLE = "announcements"
    }

    // ==================== Validation Constants ====================

    object Validation {
        const val MIN_PIN_LENGTH = 5
        const val MAX_PIN_LENGTH = 10
        const val MIN_PHONE_LENGTH = 9
        const val MAX_PHONE_LENGTH = 12
        const val MAX_MESSAGE_LENGTH = 500
        const val MAX_NOTES_LENGTH = 500
        const val MAX_NAME_LENGTH = 50
        const val MIN_NAME_LENGTH = 2
        const val MAX_FILE_SIZE_MB = 5
        const val MAX_ATTACHMENTS = 5
        const val MIN_WEIGHT_KG = 0.01
        const val MAX_WEIGHT_KG = 1000.0
        const val MAX_PLOT_SIZE_ACRES = 1000.0
    }

    // ==================== Date & Time Constants ====================

    object DateTime {
        const val DATE_FORMAT = "dd MMM yyyy"
        const val TIME_FORMAT = "HH:mm"
        const val DATE_TIME_FORMAT = "dd MMM yyyy, HH:mm"
        const val FULL_DATE_FORMAT = "EEEE, dd MMMM yyyy"
        const val MONTH_YEAR_FORMAT = "MMMM yyyy"
        const val API_DATE_FORMAT = "yyyy-MM-dd"
        const val API_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        const val ONE_DAY_MS = 24 * 60 * 60 * 1000L
        const val ONE_WEEK_MS = 7 * ONE_DAY_MS
        const val ONE_MONTH_MS = 30 * ONE_DAY_MS
        const val ONE_YEAR_MS = 365 * ONE_DAY_MS
    }

    // ==================== UI Constants ====================

    object UI {
        const val SPLASH_DELAY_MS = 2000L
        const val TOAST_DURATION_SHORT = 2000
        const val TOAST_DURATION_LONG = 3500
        const val DEBOUNCE_DELAY_MS = 300L
        const val TYPING_INDICATOR_TIMEOUT_MS = 3000L
        const val AUTO_MARK_READ_DELAY_MS = 2000L
        const val PULL_TO_REFRESH_OFFSET_DP = 80
        const val DEFAULT_PAGE_SIZE = 20
        const val ANIMATION_DURATION_MS = 300L
    }

    // ==================== Error Messages ====================

    object ErrorMessages {
        const val NETWORK_ERROR = "No internet connection. Please check your network settings."
        const val SERVER_ERROR = "Server error. Please try again later."
        const val UNKNOWN_ERROR = "An unexpected error occurred. Please try again."
        const val SESSION_EXPIRED = "Your session has expired. Please login again."
        const val INVALID_CREDENTIALS = "Invalid phone number or PIN. Please try again."
        const val EMPTY_FIELD = "This field cannot be empty"
        const val INVALID_PHONE = "Please enter a valid phone number"
        const val INVALID_PIN = "PIN must be between 5 and 10 digits"
        const val PIN_MISMATCH = "PINs do not match"
        const val WEAK_PIN = "Please choose a stronger PIN"
        const val FILE_TOO_LARGE = "File size exceeds the maximum limit"
        const val INVALID_FILE_TYPE = "This file type is not supported"
        const val MAX_FILES_EXCEEDED = "You can only attach up to 5 files"
    }

    // ==================== Success Messages ====================

    object SuccessMessages {
        const val LOGIN_SUCCESS = "Login successful"
        const val LOGOUT_SUCCESS = "Logged out successfully"
        const val PIN_CHANGED = "PIN changed successfully"
        const val DELIVERY_RECORDED = "Delivery recorded successfully"
        const val FERTILIZER_APPLIED = "Fertilizer application submitted successfully"
        const val TRANSCRIPT_GENERATED = "Transcript generated successfully"
        const val TRANSCRIPT_SHARED = "Transcript shared successfully"
        const val ISSUE_REPORTED = "Issue reported successfully. We'll get back to you soon."
        const val MESSAGE_SENT = "Message sent"
        const val NOTIFICATION_MARKED_READ = "Notification marked as read"
        const val ALL_NOTIFICATIONS_READ = "All notifications marked as read"
        const val PROFILE_UPDATED = "Profile updated successfully"
    }

    // ==================== Intent Keys ====================

    object IntentKeys {
        const val DELIVERY_ID = "delivery_id"
        const val FERTILIZER_ID = "fertilizer_id"
        const val FINANCE_ID = "finance_id"
        const val NOTIFICATION_ID = "notification_id"
        const val ANNOUNCEMENT_ID = "announcement_id"
        const val MESSAGE_ID = "message_id"
        const val FILTER_TYPE = "filter_type"
        const val YEAR = "year"
        const val MONTH = "month"
        const val FROM_NOTIFICATION = "from_notification"
        const val TRANSCRIPT_URL = "transcript_url"
    }

    // ==================== Notification Channels ====================

    object NotificationChannels {
        const val DELIVERY_CHANNEL_ID = "delivery_notifications"
        const val DELIVERY_CHANNEL_NAME = "Delivery Notifications"

        const val FERTILIZER_CHANNEL_ID = "fertilizer_notifications"
        const val FERTILIZER_CHANNEL_NAME = "Fertilizer Notifications"

        const val PAYMENT_CHANNEL_ID = "payment_notifications"
        const val PAYMENT_CHANNEL_NAME = "Payment Notifications"

        const val ANNOUNCEMENT_CHANNEL_ID = "announcement_notifications"
        const val ANNOUNCEMENT_CHANNEL_NAME = "Announcements"

        const val SUPPORT_CHANNEL_ID = "support_notifications"
        const val SUPPORT_CHANNEL_NAME = "Support Messages"
    }

    // ==================== File & Media Constants ====================

    object Files {
        const val PROVIDER_AUTHORITY = "com.farmer.chaitrack.fileprovider"
        const val TEMP_IMAGE_NAME = "temp_profile_image.jpg"
        const val TRANSCRIPT_DIR = "transcripts"
        const val CACHE_DIR = "cache"
        const val PROFILE_IMAGES_DIR = "profile_images"

        val SUPPORTED_IMAGE_TYPES = listOf("image/jpeg", "image/png", "image/jpg")
        val SUPPORTED_DOCUMENT_TYPES = listOf("application/pdf", "application/msword", "text/plain")
    }

    // ==================== Default Values ====================

    object Defaults {
        const val DEFAULT_PRICE_PER_KG = 200.0
        const val DEFAULT_PLOT_SIZE = 2.0
        const val DEFAULT_PIN = "00000" // Placeholder, actual default is user's phone number
        const val FERTILIZER_BAGS_PER_ACRE = 2
        const val NOTIFICATION_RETENTION_DAYS = 30
        const val MESSAGE_RETENTION_DAYS = 90
    }

    // ==================== Request Codes ====================

    object RequestCodes {
        const val PICK_IMAGE = 1001
        const val TAKE_PHOTO = 1002
        const val PICK_FILE = 1003
        const val PERMISSION_CAMERA = 2001
        const val PERMISSION_STORAGE = 2002
        const val PERMISSION_NOTIFICATIONS = 2003
    }

    // ==================== Country/Region Constants ====================

    object Region {
        const val COUNTRY_CODE = "KE"
        const val COUNTRY_NAME = "Kenya"
        const val PHONE_PREFIX = "+254"
        const val PHONE_PREFIX_NUMERIC = "254"
        const val CURRENCY_CODE = "KES"
        const val CURRENCY_SYMBOL = "KES"
        const val TIMEZONE = "Africa/Nairobi"
    }
}