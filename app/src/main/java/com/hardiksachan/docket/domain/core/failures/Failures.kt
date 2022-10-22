package com.hardiksachan.docket.domain.core.failures

sealed class ValueFailure<out T> {
    data class InvalidEmail(val failedValue: String) : ValueFailure<String>()
    data class ShortPassword(val failedValue: String) : ValueFailure<String>()
}