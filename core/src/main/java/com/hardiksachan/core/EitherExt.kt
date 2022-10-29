package com.hardiksachan.core

import arrow.core.Either
import arrow.core.right

inline fun <A, B, C> Either<A, B>.flatMapLeft(
    f: (A) -> Either<C, B>
): Either<C, B> = fold({ f(it) }, { it.right() })