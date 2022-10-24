package com.hardiksachan.docket.auth.auth_domain

import arrow.core.Either
import com.hardiksachan.docket.auth.auth_domain.validators.validatePassword
import com.hardiksachan.docket.core.ValueObject
import com.hardiksachan.docket.core.failures.ValueFailure

class Password private constructor(value: Either<ValueFailure<String>, String>) :
    ValueObject<String>(value) {
    companion object {
        fun create(password: String) = Password(validatePassword(password))
    }
}

