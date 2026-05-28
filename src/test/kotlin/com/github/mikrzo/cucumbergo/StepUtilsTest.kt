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
    fun toGoQuotedLiteralSimple() {
        assertEquals("\"hello world\"", toGoQuotedLiteral("hello world"))
    }

    @Test
    fun toGoQuotedLiteralEscapesBackslash() {
        assertEquals("\"a\\\\b\"", toGoQuotedLiteral("a\\b"))
    }

    @Test
    fun toGoQuotedLiteralEscapesDoubleQuote() {
        assertEquals("\"say \\\"hi\\\"\"", toGoQuotedLiteral("say \"hi\""))
    }

    @Test
    fun toGoBacktickLiteralSimple() {
        assertEquals("`hello world`", toGoBacktickLiteral("hello world"))
    }

    @Test
    fun toGoBacktickLiteralFallsBackToQuotedWhenContainsBacktick() {
        assertEquals("\"has `backtick`\"", toGoBacktickLiteral("has `backtick`"))
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
