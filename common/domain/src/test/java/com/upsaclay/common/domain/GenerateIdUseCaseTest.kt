package com.upsaclay.common.domain

import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotEquals

class GenerateIdUseCaseTest {
    private lateinit var generateIdUseCase: GenerateIdUseCase

    @Before
    fun setUp() {
        generateIdUseCase = GenerateIdUseCase
    }

    @Test
    fun generateIdUseCase_stringId_should_generate_unique_id() {
        for (i in 0..100000) {
            // When
            val id1 = generateIdUseCase.stringId
            val id2 = generateIdUseCase.stringId

            // Then
            assertNotEquals(id1, id2)
        }
    }

    @Test
    fun generateIdUseCase_longId_should_generate_unique_id() {
        for (i in 0..100000) {
            // When
            val id1 = generateIdUseCase.longId
            val id2 = generateIdUseCase.longId

            // Then
            assertNotEquals(id1, id2)
        }
    }
}