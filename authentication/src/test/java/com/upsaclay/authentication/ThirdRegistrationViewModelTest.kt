package com.upsaclay.authentication

import com.upsaclay.authentication.domain.usecase.RegisterUseCase
import com.upsaclay.authentication.presentation.registration.third.ThirdRegistrationViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ThirdRegistrationViewModelTest {
    private val registerUseCase: RegisterUseCase = mockk()

    private lateinit var thirdRegistrationViewModel: ThirdRegistrationViewModel
    
    private val testDispatcher = UnconfinedTestDispatcher()
    private val firstName = "John"
    private val lastName = "Doe"
    private val schoolLevel = "Bachelor"
    private val email = "email@example.com"
    private val password = "password1234"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        thirdRegistrationViewModel = ThirdRegistrationViewModel(
            registerUseCase = registerUseCase
        )

        coEvery { registerUseCase(any(), any(), any(), any(), any()) } returns Unit
    }

    @Test
    fun onEmailChange_should_update_email() {
        // When
        thirdRegistrationViewModel.onEmailChange(email)

        // Then
        assertEquals(email, thirdRegistrationViewModel.uiState.value.email)
    }

    @Test
    fun onPasswordChange_should_update_password() {
        // When
        thirdRegistrationViewModel.onPasswordChange(password)

        // Then
        assertEquals(password, thirdRegistrationViewModel.uiState.value.password)
    }

    @Test
    fun register_should_register_user() = runTest {
        // Given
        thirdRegistrationViewModel.onEmailChange(email)
        thirdRegistrationViewModel.onPasswordChange(password)

        // When
        thirdRegistrationViewModel.register(firstName, lastName, schoolLevel)

        // Then
        coVerify { registerUseCase(email, password, firstName, lastName, schoolLevel) }
    }

    @Test
    fun register_should_set_email_error_when_email_is_empty() {
        // Given
        thirdRegistrationViewModel.onEmailChange("")
        thirdRegistrationViewModel.onPasswordChange(password)

        // When
        thirdRegistrationViewModel.register(firstName, lastName, schoolLevel)

        // Then
        assertNotNull(thirdRegistrationViewModel.uiState.value.email)
    }

    @Test
    fun register_should_set_email_error_when_email_format_is_incorrect() {
        // Given
        thirdRegistrationViewModel.onEmailChange("email")
        thirdRegistrationViewModel.onPasswordChange(password)

        // When
        thirdRegistrationViewModel.register(firstName, lastName, schoolLevel)

        // Then
        assertNotNull(thirdRegistrationViewModel.uiState.value.email)
    }

    @Test
    fun register_should_set_password_error_when_password_is_empty() {
        // Given
        thirdRegistrationViewModel.onEmailChange(email)
        thirdRegistrationViewModel.onPasswordChange("")

        // When
        thirdRegistrationViewModel.register(firstName, lastName, schoolLevel)

        // Then
        assertNotNull(thirdRegistrationViewModel.uiState.value.password)
    }

    @Test
    fun validateInputs_should_return_false_when_password_length_is_shorter_than_8() {
        // Given
        thirdRegistrationViewModel.onEmailChange(email)
        thirdRegistrationViewModel.onPasswordChange("pass")

        // When
        thirdRegistrationViewModel.register(firstName, lastName, schoolLevel)

        // Then
        assertNotNull(thirdRegistrationViewModel.uiState.value.password)
    }
}