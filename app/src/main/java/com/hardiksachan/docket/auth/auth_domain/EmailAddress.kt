package com.hardiksachan.docket.auth.auth_domain

import arrow.core.Either
import com.hardiksachan.core.ValueObject
import com.hardiksachan.core.failures.ValueFailure
import com.hardiksachan.docket.auth.auth_domain.validators.validateEmailAddress

class EmailAddress private constructor(value: Either<ValueFailure<String>, String>) :
    ValueObject<String>(value) {
        companion object {
            fun create(email: String) = EmailAddress(validateEmailAddress(email))
        }
}