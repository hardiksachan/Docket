package com.hardiksachan.auth_domain

import arrow.core.Either
import com.hardiksachan.auth_domain.validators.validatePassword
import com.hardiksachan.core.ValueObject
import com.hardiksachan.core.failures.ValueFailure

class Password private constructor(value: Either<ValueFailure<String>, String>) :
    ValueObject<String>(value) {
    companion object {
        fun create(password: String) = Password(validatePassword(password))
    }
}

