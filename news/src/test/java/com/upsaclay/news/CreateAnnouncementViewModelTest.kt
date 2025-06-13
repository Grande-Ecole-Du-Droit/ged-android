package com.upsaclay.news

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase
import com.upsaclay.news.presentation.announcement.createannouncement.CreateAnnouncementViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CreateAnnouncementViewModelTest {
    private val userRepository: UserRepository = mockk()
    private val createAnnouncementUseCase: CreateAnnouncementUseCase = mockk()

    private lateinit var createAnnouncementViewModel: CreateAnnouncementViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val title = "title"
    private val content = "content"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { userRepository.user } returns MutableStateFlow(userFixture)
        every { userRepository.currentUser } returns userFixture
        coEvery { createAnnouncementUseCase(any()) } returns Unit

        createAnnouncementViewModel = CreateAnnouncementViewModel(
            userRepository = userRepository,
            createAnnouncementUseCase = createAnnouncementUseCase
        )
    }

    @Test
    fun default_values_are_correct() {
        assertEquals("", createAnnouncementViewModel.uiState.value.title)
        assertEquals("", createAnnouncementViewModel.uiState.value.content)
    }

    @Test
    fun updateTitle_should_on_titleChange() {
        // When
        createAnnouncementViewModel.onTitleChange(title)

        // Then
        assertEquals(title, createAnnouncementViewModel.uiState.value.title)
    }

    @Test
    fun updateContent_should_on_contentChange() {
        // When
        createAnnouncementViewModel.onContentChange(content)

        // Then
        assertEquals(content, createAnnouncementViewModel.uiState.value.content)
    }

    @Test
    fun createAnnouncement_should_create_announcement() {
        // When
        createAnnouncementViewModel.createAnnouncement()

        // Then
        coVerify { createAnnouncementUseCase(any())}
    }

    @Test
    fun createAnnouncement_should_not_create_announcement_when_user_is_null() {
        // Given
        every { userRepository.currentUser } returns null
        createAnnouncementViewModel = CreateAnnouncementViewModel(
            userRepository = userRepository,
            createAnnouncementUseCase = createAnnouncementUseCase
        )

        // When
        createAnnouncementViewModel.createAnnouncement()

        // Then
        coVerify(exactly = 0) { createAnnouncementUseCase(any())}
    }

    @Test
    fun createEnabled_should_be_false_when_content_is_blank() {
        // Given
        createAnnouncementViewModel.onTitleChange("title")
        createAnnouncementViewModel.onContentChange("")

        // Then
        assertEquals(false, createAnnouncementViewModel.uiState.value.createEnabled)
    }

    @Test
    fun createEnabled_should_be_true_when_content_is_not_blank() {
        // Given
        createAnnouncementViewModel.onTitleChange("title")
        createAnnouncementViewModel.onContentChange("content")

        // Then
        assertEquals(true, createAnnouncementViewModel.uiState.value.createEnabled)
    }
}