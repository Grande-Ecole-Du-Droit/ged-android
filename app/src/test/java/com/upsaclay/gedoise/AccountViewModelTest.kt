package com.upsaclay.gedoise

import android.net.Uri
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.DeleteProfilePictureUseCase
import com.upsaclay.common.domain.usecase.UpdateProfilePictureUseCase
import com.upsaclay.common.domain.userFixture
import com.upsaclay.gedoise.presentation.profile.account.AccountScreenState
import com.upsaclay.gedoise.presentation.profile.account.AccountViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AccountViewModelTest {
    private val updateProfilePictureUseCase: UpdateProfilePictureUseCase = mockk()
    private val deleteProfilePictureUseCase: DeleteProfilePictureUseCase = mockk()
    private val userRepository: UserRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var accountViewModel: AccountViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val uri: Uri = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { connectivityObserver.isConnected } returns true
        every { userRepository.user } returns MutableStateFlow(userFixture)
        coEvery { updateProfilePictureUseCase(any(), any()) } returns Unit
        coEvery { deleteProfilePictureUseCase(any(), any()) } returns Unit

        accountViewModel = AccountViewModel(
            updateProfilePictureUseCase = updateProfilePictureUseCase,
            deleteProfilePictureUseCase = deleteProfilePictureUseCase,
            connectivityObserver = connectivityObserver,
            userRepository = userRepository
        )
    }

    @Test
    fun onProfilePictureUriChange_should_update_profile_picture_uri() {
        // When
        accountViewModel.onProfilePictureUriChange(uri)

        // Then
        assert(accountViewModel.uiState.value.profilePictureUri == uri)
    }

    @Test
    fun onScreenStateChange_should_update_screen_state() {
        // Given
        val screenState = AccountScreenState.EDIT

        // When
        accountViewModel.onScreenStateChange(screenState)

        // Then
        assertEquals(screenState, accountViewModel.uiState.value.screenState)
    }

    @Test
    fun updateUserProfilePicture_should_update_profile_picture_when_uri_is_not_null() = runTest {
        // Given
        accountViewModel.onProfilePictureUriChange(uri)

        // When
        accountViewModel.updateProfilePicture()

        // Then
        coVerify { updateProfilePictureUseCase(any(), any()) }
    }

    @Test
    fun updateProfilePicture_should_not_be_executed_when_no_connection() = runTest {
        // Given
        every { connectivityObserver.isConnected } returns false
        accountViewModel.onProfilePictureUriChange(uri)

        // When
        accountViewModel.updateProfilePicture()

        // Then
        coVerify(exactly = 0) { updateProfilePictureUseCase(any(), any()) }
    }

    @Test
    fun deleteProfilePicture_should_reset_profile_picture_uri() = runTest {
        // Given
        accountViewModel.onProfilePictureUriChange(uri)

        // When
        accountViewModel.deleteProfilePicture()

        // Then
        assertEquals(null, accountViewModel.uiState.value.profilePictureUri)
    }

    @Test
    fun deleteProfilePicture_should_not_be_executed_when_no_connection() = runTest {
        // Given
        every { connectivityObserver.isConnected } returns false
        accountViewModel.onProfilePictureUriChange(uri)

        // When
        accountViewModel.deleteProfilePicture()

        // Then
        coVerify(exactly = 0) { deleteProfilePictureUseCase(any(), any()) }
    }
}