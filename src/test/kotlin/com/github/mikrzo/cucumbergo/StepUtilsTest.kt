package com.github.mikrzo.cucumbergo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StepUtilsTest {

    @Test
    fun testCheckIdentifierNameGiven() {
        assertTrue(StepUtils.checkIdentifierName("Given"))
    }

    @Test
    fun testCheckIdentifierNameWhen() {
        assertTrue(StepUtils.checkIdentifierName("When"))
    }

    @Test
    fun testCheckIdentifierNameThen() {
        assertTrue(StepUtils.checkIdentifierName("Then"))
    }

    @Test
    fun testCheckIdentifierNameStep() {
        assertTrue(StepUtils.checkIdentifierName("Step"))
    }

    @Test
    fun testCheckIdentifierNameLowercaseRejected() {
        assertFalse(StepUtils.checkIdentifierName("given"))
    }

    @Test
    fun testCheckIdentifierNameAndRejected() {
        assertFalse(StepUtils.checkIdentifierName("And"))
    }

    @Test
    fun testCheckIdentifierNameEmpty() {
        assertFalse(StepUtils.checkIdentifierName(""))
    }

    @Test
    fun testToCamelCaseTwoWords() {
        assertEquals("HelloWorld", ToCamelCase("hello world"))
    }

    @Test
    fun testToCamelCaseSingleWord() {
        assertEquals("Single", ToCamelCase("single"))
    }

    @Test
    fun testToCamelCaseAlreadyCapitalized() {
        assertEquals("AlreadyCapitalized", ToCamelCase("already Capitalized"))
    }

    @Test
    fun testToCamelCaseEmpty() {
        assertEquals("", ToCamelCase(""))
    }
}
