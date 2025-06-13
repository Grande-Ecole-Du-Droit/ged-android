package com.upsaclay.authentication.domain

import com.upsaclay.authentication.domain.usecase.GenerateHashUseCase
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotEquals

class GenerateHashUseCaseTest {
    private lateinit var generateHashUseCase: GenerateHashUseCase

    @Before
    fun setUp() {
        generateHashUseCase = GenerateHashUseCase()
    }

    @Test
    fun generateHashUseCase_should_not_return_same_value() {
        for (i in 0..1000) {
            val hash1 = generateHashUseCase()
            val hash2 = generateHashUseCase()
            assertNotEquals(hash1, hash2)
        }
    }
}