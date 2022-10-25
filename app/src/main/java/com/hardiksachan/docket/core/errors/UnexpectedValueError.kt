package com.hardiksachan.docket.core.errors

import com.hardiksachan.docket.core.failures.ValueFailure

class UnexpectedValueError (private val valueFailure: ValueFailure<*>) : Error() {
    override val message: String
        get() = "Encountered a ValueFailure at unrecoverable point. Terminating. Failure was $valueFailure"
}