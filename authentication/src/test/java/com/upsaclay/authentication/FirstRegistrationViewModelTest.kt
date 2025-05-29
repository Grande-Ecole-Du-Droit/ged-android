package com.upsaclay.authentication

import com.upsaclay.authentication.presentation.registration.firstregistration.FirstRegistrationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class FirstRegistrationViewModelTest {
    private lateinit var firstRegistrationViewModel: FirstRegistrationViewModel

    private val testDispatcher = UnconfinedTestDispatcher()
    private val firstName = "John"
    private val lastName = "Doe"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        firstRegistrationViewModel = FirstRegistrationViewModel()
    }

    @Test
    fun onFirstNameChange_should_on_update_first_name() {
        // When
        firstRegistrationViewModel.onFirstNameChange(firstName)

        // Then
        assertEquals(firstName, firstRegistrationViewModel.uiState.value.firstName)
    }

    @Test
    fun onLastNameChange_should_on_update_last_Name() {
        // When
        firstRegistrationViewModel.onLastNameChange(lastName)

        // Then
        assertEquals(lastName, firstRegistrationViewModel.uiState.value.lastName)
    }

    @Test
    fun validateInputs_should_return_true_when_not_empty() {
        // Given
        firstRegistrationViewModel.onFirstNameChange(firstName)
        firstRegistrationViewModel.onLastNameChange(lastName)

        // When
        val result = firstRegistrationViewModel.validateInputs()

        // Then
        assertEquals(true, result)
    }

    @Test
    fun validateInputs_should_return_false_when_empty() {
        // When
        val result = firstRegistrationViewModel.validateInputs()

        // Then
        assertEquals(false, result)
    }

    @Test
    fun validateInputs_should_return_false_when_blank() {
        // Given
        firstRegistrationViewModel.onFirstNameChange("  ")
        firstRegistrationViewModel.onLastNameChange("")

        // When
        val result = firstRegistrationViewModel.validateInputs()

        // Then
        assertFalse(result)
    }

    @Test
    fun validateInputs_should_return_true_when_not_blank() {
        // Given
        firstRegistrationViewModel.onFirstNameChange("  John  ")
        firstRegistrationViewModel.onLastNameChange("  Doe  ")

        // When
        val result = firstRegistrationViewModel.validateInputs()

        // Then
        assertEquals(true, result)
    }

    @Test
    fun validateInputs_should_trim_and_uppercase_names() {
        // Given
        firstRegistrationViewModel.onFirstNameChange("john ")
        firstRegistrationViewModel.onLastNameChange(" doe")

        // When
        firstRegistrationViewModel.validateInputs()

        // Then
        assertEquals(firstName, firstRegistrationViewModel.uiState.value.firstName)
        assertEquals(lastName, firstRegistrationViewModel.uiState.value.lastName)
    }
}