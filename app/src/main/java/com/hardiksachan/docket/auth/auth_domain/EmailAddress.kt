package com.hardiksachan.docket.auth.auth_domain

import arrow.core.Either
import com.hardiksachan.docket.auth.auth_domain.validators.validateEmailAddress
import com.hardiksachan.docket.core.ValueObject
import com.hardiksachan.docket.core.failures.ValueFailure

class EmailAddress private constructor(value: Either<ValueFailure<String>, String>) :
    ValueObject<String>(value) {
        companion object {
            fun create(email: String) = EmailAddress(validateEmailAddress(email))
        }
}