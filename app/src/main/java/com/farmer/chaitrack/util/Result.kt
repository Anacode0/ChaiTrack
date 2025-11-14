package com.farmer.chaitrack.util

sealed class Result<out T> {

    /**
     * Represents a successful operation with data
     * @param data The data returned from the operation
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Represents a failed operation with error information
     * @param message Human-readable error message
     * @param throwable Optional throwable for debugging
     * @param code Optional error code for specific error handling
     */
    data class Error(
        val message: String,
        val throwable: Throwable? = null,
        val code: ErrorCode? = null
    ) : Result<Nothing>()

    /**
     * Represents an operation that is currently in progress
     */
    object Loading : Result<Nothing>()

    /**
     * Predefined error codes for common error scenarios
     */
    enum class ErrorCode {
        NETWORK_ERROR,
        AUTHENTICATION_ERROR,
        AUTHORIZATION_ERROR,
        SERVER_ERROR,
        DATABASE_ERROR,
        VALIDATION_ERROR,
        NOT_FOUND,
        TIMEOUT,
        UNKNOWN
    }

    /**
     * Returns true if the result is Success
     */
    fun isSuccess(): Boolean = this is Success

    /**
     * Returns true if the result is Error
     */
    fun isError(): Boolean = this is Error

    /**
     * Returns true if the result is Loading
     */
    fun isLoading(): Boolean = this is Loading

    /**
     * Returns the data if Success, null otherwise
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    /**
     * Returns the data if Success, or the default value otherwise
     */
    fun getOrDefault(defaultValue: @UnsafeVariance T): T = when (this) {
        is Success -> data
        else -> defaultValue
    }

    /**
     * Returns the data if Success, or throws the error if Error
     */
    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw throwable ?: Exception(message)
        is Loading -> throw IllegalStateException("Cannot get data while loading")
    }

    /**
     * Returns the error message if Error, null otherwise
     */
    fun getErrorMessage(): String? = when (this) {
        is Error -> message
        else -> null
    }

    /**
     * Executes the given block if the result is Success
     */
    inline fun onSuccess(block: (T) -> Unit): Result<T> {
        if (this is Success) {
            block(data)
        }
        return this
    }

    /**
     * Executes the given block if the result is Error
     */
    inline fun onError(block: (String, Throwable?, ErrorCode?) -> Unit): Result<T> {
        if (this is Error) {
            block(message, throwable, code)
        }
        return this
    }

    /**
     * Executes the given block if the result is Loading
     */
    inline fun onLoading(block: () -> Unit): Result<T> {
        if (this is Loading) {
            block()
        }
        return this
    }

    /**
     * Maps the success data to a new type
     */
    inline fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(message, throwable, code)
        is Loading -> Loading
    }

    /**
     * Flat maps the success data to a new Result
     */
    inline fun <R> flatMap(transform: (T) -> Result<R>): Result<R> = when (this) {
        is Success -> transform(data)
        is Error -> Error(message, throwable, code)
        is Loading -> Loading
    }

    companion object {

        /**
         * Creates a Success result
         */
        fun <T> success(data: T): Result<T> = Success(data)

        /**
         * Creates an Error result with just a message
         */
        fun <T> error(message: String): Result<T> = Error(message)

        /**
         * Creates an Error result with message and throwable
         */
        fun <T> error(message: String, throwable: Throwable): Result<T> =
            Error(message, throwable)

        /**
         * Creates an Error result with message, throwable, and error code
         */
        fun <T> error(
            message: String,
            throwable: Throwable? = null,
            code: ErrorCode
        ): Result<T> = Error(message, throwable, code)

        /**
         * Creates a Loading result
         */
        fun <T> loading(): Result<T> = Loading

        /**
         * Wraps a suspending operation in a Result, catching any exceptions
         */
        suspend fun <T> runCatching(block: suspend () -> T): Result<T> {
            return try {
                Success(block())
            } catch (e: Exception) {
                Error(
                    message = e.message ?: "An unknown error occurred",
                    throwable = e,
                    code = mapExceptionToErrorCode(e)
                )
            }
        }

        /**
         * Wraps a regular operation in a Result, catching any exceptions
         */
        fun <T> run(block: () -> T): Result<T> {
            return try {
                Success(block())
            } catch (e: Exception) {
                Error(
                    message = e.message ?: "An unknown error occurred",
                    throwable = e,
                    code = mapExceptionToErrorCode(e)
                )
            }
        }

        /**
         * Maps common exceptions to error codes
         */
        private fun mapExceptionToErrorCode(exception: Exception): ErrorCode {
            return when (exception) {
                is java.net.UnknownHostException,
                is java.io.IOException -> ErrorCode.NETWORK_ERROR
                is java.net.SocketTimeoutException -> ErrorCode.TIMEOUT
                is IllegalArgumentException,
                is IllegalStateException -> ErrorCode.VALIDATION_ERROR
                else -> ErrorCode.UNKNOWN
            }
        }
    }
}

/**
 * Extension function to convert nullable values to Result
 */
fun <T> T?.toResult(errorMessage: String = "Value is null"): Result<T> {
    return if (this != null) {
        Result.Success(this)
    } else {
        Result.Error(errorMessage, code = Result.ErrorCode.NOT_FOUND)
    }
}

/**
 * Extension function to combine multiple results
 * Returns Success only if all results are Success
 */
fun <T> List<Result<T>>.combine(): Result<List<T>> {
    val errors = filterIsInstance<Result.Error>()
    if (errors.isNotEmpty()) {
        return Result.Error(
            message = errors.joinToString(", ") { it.message },
            throwable = errors.firstOrNull()?.throwable
        )
    }

    if (any { it is Result.Loading }) {
        return Result.Loading
    }

    val successData = filterIsInstance<Result.Success<T>>().map { it.data }
    return Result.Success(successData)
}