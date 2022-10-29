package com.hardiksachan.core

import arrow.core.Either
import com.hardiksachan.core.errors.UnexpectedValueError
import com.hardiksachan.core.failures.ValueFailure

abstract class ValueObject<out T>(val value: Either<ValueFailure<T>, T>) {
    fun isValid() = value.isRight()

    fun getOrCrash() = value.fold(
        { f -> throw UnexpectedValueError(f) },
        { r -> r }
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ValueObject<*>) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}