package com.upsaclay.news

import com.upsaclay.common.domain.usecase.GetCurrentUserUseCase
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase
import com.upsaclay.news.presentation.viewmodels.CreateAnnouncementViewModel
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
    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk()
    private val createAnnouncementUseCase: CreateAnnouncementUseCase = mockk()

    private lateinit var createAnnouncementViewModel: CreateAnnouncementViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val title = "title"
    private val content = "content"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { getCurrentUserUseCase() } returns MutableStateFlow(userFixture)
        coEvery { createAnnouncementUseCase(any()) } returns Unit

        createAnnouncementViewModel = CreateAnnouncementViewModel(
            getCurrentUserUseCase = getCurrentUserUseCase,
            createAnnouncementUseCase = createAnnouncementUseCase
        )
    }

    @Test
    fun default_values_are_correct() {
        assertEquals("", createAnnouncementViewModel.title)
        assertEquals("", createAnnouncementViewModel.content)
    }

    @Test
    fun updateTitle_should_update_title() {
        // When
        createAnnouncementViewModel.updateTitle(title)

        // Then
        assertEquals(title, createAnnouncementViewModel.title)
    }

    @Test
    fun updateContent_should_update_content() {
        // When
        createAnnouncementViewModel.updateContent(content)

        // Then
        assertEquals(content, createAnnouncementViewModel.content)
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
        every { getCurrentUserUseCase() } returns MutableStateFlow(null)
        createAnnouncementViewModel = CreateAnnouncementViewModel(
            getCurrentUserUseCase = getCurrentUserUseCase,
            createAnnouncementUseCase = createAnnouncementUseCase
        )

        // When
        createAnnouncementViewModel.createAnnouncement()

        // Then
        coVerify(exactly = 0) { createAnnouncementUseCase(any())}
    }
}