package com.hardiksachan.docket

import io.kotest.core.spec.style.StringSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ExampleUnitSpec : StringSpec({
    "addition is correct" {
        val a = 5
        val b = 7
        val sum = a + b

        expectThat(sum).isEqualTo(a + b)
    }
})