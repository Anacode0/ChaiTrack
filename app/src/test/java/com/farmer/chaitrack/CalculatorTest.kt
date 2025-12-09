package com.farmer.chaitrack

import org.junit.Test
import org.junit.Assert.*

class CalculatorTest {

    @Test
    fun `addition should return correct sum`() {
        // Given
        val calculator = Calculator()
        val a = 5
        val b = 3

        // When
        val result = calculator.add(a, b)

        // Then
        assertEquals(8, result)
    }

    @Test
    fun `subtraction should return correct difference`() {
        // Given
        val calculator = Calculator()
        val a = 10
        val b = 4

        // When
        val result = calculator.subtract(a, b)

        // Then
        assertEquals(6, result)
    }
}

class Calculator {
    fun add(a: Int, b: Int): Int = a + b
    fun subtract(a: Int, b: Int): Int = a - b
}