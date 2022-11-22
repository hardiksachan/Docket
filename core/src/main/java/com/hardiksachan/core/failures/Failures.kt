package com.hardiksachan.core.failures

sealed class ValueFailure<out T>(val failedValue: T) {
    data class InvalidEmail(val email: String) : ValueFailure<String>(email)
    data class ShortPassword(val password: String) : ValueFailure<String>(password)
    data class IllegalUUID(val uuid: String): ValueFailure<String>(uuid)
}