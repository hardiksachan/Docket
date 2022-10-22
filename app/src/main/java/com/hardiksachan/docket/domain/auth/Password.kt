package com.hardiksachan.docket.domain.auth

import arrow.core.Either
import com.hardiksachan.docket.domain.core.failures.ValueFailure
import com.hardiksachan.docket.domain.core.validators.validatePassword

@JvmInline
value class Password private constructor(val value: Either<ValueFailure<String>, String>) {
    companion object {
        fun create(password: String) = Password(validatePassword(password))
    }
}