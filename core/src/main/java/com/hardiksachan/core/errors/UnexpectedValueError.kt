package com.hardiksachan.core.errors

import com.hardiksachan.core.failures.ValueFailure

class UnexpectedValueError (private val valueFailure: ValueFailure<*>) : Error() {
    override val message: String
        get() = "Encountered a ValueFailure at unrecoverable point. Terminating. Failure was $valueFailure"
}