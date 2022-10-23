package com.hardiksachan.docket.auth.auth_domain

import arrow.core.Either
import com.hardiksachan.docket.core.failures.ValueFailure
import com.hardiksachan.docket.auth.auth_domain.validators.validatePassword

@JvmInline
value class Password private constructor(val value: Either<ValueFailure<String>, String>) {
    companion object {
        fun create(password: String) = Password(validatePassword(password))
    }
}

