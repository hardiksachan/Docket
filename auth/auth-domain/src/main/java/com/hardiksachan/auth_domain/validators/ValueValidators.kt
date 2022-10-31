package com.hardiksachan.auth_domain.validators

import arrow.core.Either
import com.hardiksachan.core.failures.ValueFailure

internal fun validateEmailAddress(input: String): Either<ValueFailure<String>, String> {
    val regex = """[\w-.]+@([\w-]+\.)+[\w-]{2,4}""".toRegex()
    if (!input.matches(regex)) {
        return Either.Left(ValueFailure.InvalidEmail(input))
    }
    return Either.Right(input)
}

internal fun validatePassword(input: String): Either<ValueFailure<String>, String> {
    if (input.length < 6) {
        return Either.Left(ValueFailure.ShortPassword(input))
    }
    return Either.Right(input)
}