package com.upsaclay.authentication

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.authentication.presentation.authentication.AuthenticationViewModel
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertNull
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
class AuthenticationViewModelTest {
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val userRepository: UserRepository = mockk()

    private lateinit var authenticationViewModel: AuthenticationViewModel
    private val email = "email@example.com"
    private val password = "password"
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        authenticationViewModel = AuthenticationViewModel(
            authenticationRepository = authenticationRepository,
            userRepository = userRepository
        )

        coEvery { authenticationRepository.loginWithEmailAndPassword(any(), any()) } returns Unit
        coEvery { authenticationRepository.setAuthenticated(any()) } returns Unit
        coEvery { userRepository.getUserWithEmail(any()) } returns userFixture
        coEvery { userRepository.storeUser(any()) } returns Unit
    }

    @Test
    fun onEmailChange_updates_email() {
        // When
        authenticationViewModel.onEmailChange(email)

        // Then
        assertEquals(email, authenticationViewModel.uiState.value.email)
    }

    @Test
    fun onPasswordChange_updates_password() {
        // When
        authenticationViewModel.onPasswordChange(password)

        // Then
        assertEquals(password, authenticationViewModel.uiState.value.password)
    }

    @Test
    fun login_should_reset_password_when_exception_is_thrown() = runTest {
        // Given
        coEvery { authenticationRepository.loginWithEmailAndPassword(any(), any()) } throws Exception()
        authenticationViewModel.onPasswordChange(password)
        authenticationViewModel.onEmailChange(email)

        // When
        authenticationViewModel.login()

        // Then
        assertEquals("", authenticationViewModel.uiState.value.password)
    }

    @Test
    fun login_should_set_email_error_when_email_is_blank() {
        // Given
        authenticationViewModel.onEmailChange("")

        // When
        authenticationViewModel.login()

        // Then
        assertNotNull(authenticationViewModel.uiState.value.emailError)
    }

    @Test
    fun login_should_set_password_error_when_password_is_blank() {
        // Given
        authenticationViewModel.onEmailChange(email)
        authenticationViewModel.onPasswordChange("")

        // When
        authenticationViewModel.login()

        // Then
        assertNotNull(authenticationViewModel.uiState.value.passwordError)
    }

    @Test
    fun login_should_set_email_error_when_email_have_an_invalid_format() {
        // Given
        authenticationViewModel.onEmailChange("email")
        authenticationViewModel.onPasswordChange(password)

        // When
        authenticationViewModel.login()

        // Then
        assertNotNull(authenticationViewModel.uiState.value.email)
    }

    @Test
    fun login_not_set_email_error_and_password_error_when_email_and_password_are_not_blank_and_they_have_correct_format() {
        // Given
        authenticationViewModel.onEmailChange(email)
        authenticationViewModel.onPasswordChange(password)

        // When
        authenticationViewModel.login()

        // Then
        assertNull(authenticationViewModel.uiState.value.emailError)
        assertNull(authenticationViewModel.uiState.value.passwordError)
    }
}