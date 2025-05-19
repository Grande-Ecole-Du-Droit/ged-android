package com.upsaclay.common.domain

import com.upsaclay.common.domain.usecase.GenerateRandomIdUseCase
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotEquals

class GenerateRandomIdUseCaseTest {
    private lateinit var generateIdUseCase: GenerateRandomIdUseCase

    @Before
    fun setUp() {
        generateIdUseCase = GenerateRandomIdUseCase
    }

    @Test
    fun generateIdUseCase_should_generate_unique_id() {
        for (i in 0..1000) {
            // When
            val id1 = generateIdUseCase.stringId
            val id2 = generateIdUseCase.stringId

            // Then
            assertNotEquals(id1, id2)
        }
    }
}