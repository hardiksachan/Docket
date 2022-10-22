package com.hardiksachan.docket.domain.auth

import arrow.core.Either
import com.hardiksachan.docket.domain.core.failures.ValueFailure
import com.hardiksachan.docket.domain.core.validators.validateEmailAddress

@JvmInline
value class EmailAddress private constructor(val value: Either<ValueFailure<String>, String>) {
    companion object {
        fun create(email: String) = EmailAddress(validateEmailAddress(email))
    }
}