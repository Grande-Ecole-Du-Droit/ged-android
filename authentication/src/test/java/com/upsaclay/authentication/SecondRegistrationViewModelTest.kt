package com.upsaclay.authentication

import com.upsaclay.authentication.presentation.registration.secondregistration.SecondRegistrationViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SecondRegistrationViewModelTest {
    private lateinit var secondRegistrationViewModel: SecondRegistrationViewModel

    private val testDispatcher = UnconfinedTestDispatcher()
    private val schoolLevels = listOf("GED 1", "GED 2", "GED 3", "GED 4")
    private val schoolLevel = "GED 1"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        secondRegistrationViewModel = SecondRegistrationViewModel()
    }

    @Test
    fun default_values_are_correct() {
        // Then
        assertEquals(schoolLevels, secondRegistrationViewModel.schoolLevels)
        assertEquals(schoolLevel, secondRegistrationViewModel.schoolLevel.value)
    }

    @Test
    fun updateSchoolLevel_should_on_schoolLevelChange() {
        // Given
        val schoolLevel = "GED 2"

        // When
        secondRegistrationViewModel.onSchoolLevelChange(schoolLevel)

        // Then
        assertEquals(schoolLevel, secondRegistrationViewModel.schoolLevel.value)
    }
}