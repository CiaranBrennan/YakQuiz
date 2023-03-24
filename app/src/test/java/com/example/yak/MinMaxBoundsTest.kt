package com.example.yak

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MinMaxBoundsTest {
    private val minMaxBounds: MinMaxBounds = MinMaxBounds()

    @Test
    fun within_bounds_low() {
        assertTrue(minMaxBounds.isInRange(5, 10, 5))
    }

    @Test
    fun within_bounds_mid() {
        assertTrue(minMaxBounds.isInRange(5, 10, 8))
    }

    @Test
    fun within_bounds_high() {
        assertTrue(minMaxBounds.isInRange(5, 10, 10))
    }

    @Test
    fun out_of_bounds_low() {
        assertFalse(minMaxBounds.isInRange(5, 10, 4))
    }

    @Test
    fun out_of_bounds_high() {
        assertFalse(minMaxBounds.isInRange(5, 10, 11))
    }
}