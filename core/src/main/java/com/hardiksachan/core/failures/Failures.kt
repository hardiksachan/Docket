package com.hardiksachan.core.failures

sealed class ValueFailure<out T>(val failedValue: T) {
    class InvalidEmail(failedValue: String) : ValueFailure<String>(failedValue)
    class ShortPassword(failedValue: String) : ValueFailure<String>(failedValue)
}