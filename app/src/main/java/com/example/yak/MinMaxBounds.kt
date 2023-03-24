package com.example.yak

import android.text.InputFilter
import android.text.Spanned

class MinMaxBounds : InputFilter {
    private var minBound: Int
    private var maxBound: Int

    // Constructor for testing purposes, all INT values within range if called for filtering
    constructor() {
        this.minBound = Int.MIN_VALUE
        this.maxBound = Int.MAX_VALUE
    }

    // Handle limits set as ints
    constructor(min: Int, max: Int) {
        this.minBound = min
        this.maxBound = max
    }

    // Override filter function
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            // Remove substring to be replaced
            var newVal = dest.toString().substring(0, dstart) + dest.toString()
                .substring(dend, dest.toString().length)
            // Add new string
            newVal = newVal.substring(0, dstart) + source.toString() + newVal.substring(
                dstart,
                newVal.length
            )
            val input = newVal.toInt()
            if (isInRange(minBound, maxBound, input)) return null
        } catch (nfe: NumberFormatException) {
        }
        return ""
    }

    // Check that value is within range of new imposed limits
    fun isInRange(inputMin: Int, inputMax: Int, userInput: Int): Boolean {
        return if (inputMax > inputMin) userInput in inputMin..inputMax else userInput in inputMax..inputMin
    }
}