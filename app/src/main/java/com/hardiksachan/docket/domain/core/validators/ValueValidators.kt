package com.hardiksachan.docket.domain.core.validators

import arrow.core.Either
import com.hardiksachan.docket.domain.core.failures.ValueFailure

fun validateEmailAddress(input: String): Either<ValueFailure<String>, String> {
    val regex = """[\w-.]+@([\w-]+\.)+[\w-]{2,4}""".toRegex()
    if (!input.matches(regex)) {
        return Either.Left(ValueFailure.InvalidEmail(input))
    }
    return Either.Right(input)
}

fun validatePassword(input: String): Either<ValueFailure<String>, String> {
    if (input.length < 6) {
        return Either.Left(ValueFailure.ShortPassword(input))
    }
    return Either.Right(input)
}