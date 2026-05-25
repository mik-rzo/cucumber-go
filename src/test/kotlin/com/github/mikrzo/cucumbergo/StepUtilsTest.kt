package com.github.mikrzo.cucumbergo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StepUtilsTest {

    @Test
    fun checkIdentifierNameGiven() {
        assertTrue(StepUtils.checkIdentifierName("Given"))
    }

    @Test
    fun checkIdentifierNameWhen() {
        assertTrue(StepUtils.checkIdentifierName("When"))
    }

    @Test
    fun checkIdentifierNameThen() {
        assertTrue(StepUtils.checkIdentifierName("Then"))
    }

    @Test
    fun checkIdentifierNameStep() {
        assertTrue(StepUtils.checkIdentifierName("Step"))
    }

    @Test
    fun checkIdentifierNameLowercaseRejected() {
        assertFalse(StepUtils.checkIdentifierName("given"))
    }

    @Test
    fun checkIdentifierNameAndRejected() {
        assertFalse(StepUtils.checkIdentifierName("And"))
    }

    @Test
    fun checkIdentifierNameEmpty() {
        assertFalse(StepUtils.checkIdentifierName(""))
    }

    @Test
    fun toPascalCaseTwoWords() {
        assertEquals("HelloWorld", toPascalCase("hello world"))
    }

    @Test
    fun toPascalCaseSingleWord() {
        assertEquals("Single", toPascalCase("single"))
    }

    @Test
    fun toPascalCaseAlreadyCapitalized() {
        assertEquals("AlreadyCapitalized", toPascalCase("already Capitalized"))
    }

    @Test
    fun toPascalCaseEmpty() {
        assertEquals("", toPascalCase(""))
    }
}
