package com.upsaclay.common.domain

import com.upsaclay.common.domain.usecase.VerifyEmailFormatUseCase
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class VerifyEmailFormatUseCaseTest {
    private lateinit var verifyEmailFormatUseCase: VerifyEmailFormatUseCase

    @Before
    fun setUp() {
        verifyEmailFormatUseCase = VerifyEmailFormatUseCase
    }

    @Test
    fun verifyEmailFormatUseCase_should_return_true_when_email_format_is_correct() {
        // When
        val result = verifyEmailFormatUseCase(userFixture.email)

        // Then
        assertEquals(true, result)
    }

    @Test
    fun verifyEmailFormatUseCase_should_return_false_when_email_not_contains_at() {
        // When
        val result = verifyEmailFormatUseCase("email.com")

        // Then
        assertEquals(false, result)
    }

    @Test
    fun verifyEmailFormatUseCase_should_return_false_when_email_not_contains_dot_after_at() {
        // When
        val result = verifyEmailFormatUseCase("email@com")

        // Then
        assertEquals(false, result)
    }

    @Test
    fun verifyEmailFormatUseCase_should_return_false_when_email_not_contains_string_after_last_dot() {
        // When
        val result = verifyEmailFormatUseCase("email@com.")

        // Then
        assertEquals(false, result)
    }
}