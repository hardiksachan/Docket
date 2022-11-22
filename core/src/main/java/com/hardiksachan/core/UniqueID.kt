package com.hardiksachan.core

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.hardiksachan.core.failures.ValueFailure
import kotlinx.uuid.UUID

class UniqueID private constructor(
    value: Either<ValueFailure<String>, String>
) : ValueObject<String>(value) {
    companion object {
        fun create(): UniqueID = UniqueID(UUID().toString().right())
        fun fromUniqueString(id: String): UniqueID = try {
            UniqueID(UUID(id).toString().right())
        } catch (t: Throwable) {
            UniqueID(ValueFailure.IllegalUUID(id).left())
        }
    }
}