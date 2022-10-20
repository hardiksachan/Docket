package com.hardiksachan.docket

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        expectThat(2 + 2)
            .isEqualTo(4)
    }
}