package com.hardiksachan.docket.auth.auth_domain

import arrow.core.Either
import com.hardiksachan.docket.core.failures.ValueFailure
import com.hardiksachan.docket.core.validators.validateEmailAddress

@JvmInline
value class EmailAddress private constructor(val value: Either<ValueFailure<String>, String>) {
    companion object {
        fun create(email: String) = EmailAddress(validateEmailAddress(email))
    }
}