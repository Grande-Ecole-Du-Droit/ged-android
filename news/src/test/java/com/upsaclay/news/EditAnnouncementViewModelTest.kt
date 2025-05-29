package com.upsaclay.news

import com.upsaclay.news.domain.announcementFixture
import com.upsaclay.news.domain.usecase.UpdateAnnouncementUseCase
import com.upsaclay.news.presentation.announcement.editannouncement.EditAnnouncementViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class EditAnnouncementViewModelTest {
    private val updateAnnouncementUseCase: UpdateAnnouncementUseCase = mockk()

    private lateinit var editAnnouncementViewModel: EditAnnouncementViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val title = "Title"
    private val content = "Content"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { updateAnnouncementUseCase(any()) } returns Unit

        editAnnouncementViewModel = EditAnnouncementViewModel(
            announcement = announcementFixture,
            updateAnnouncementUseCase = updateAnnouncementUseCase
        )
    }

    @Test
    fun updateTitle_should_on_titleChange() {
        // When
        editAnnouncementViewModel.onTitleChange(title)

        // Then
        assertEquals(title, editAnnouncementViewModel.uiState.value.title)
    }

    @Test
    fun updateContent_should_on_contentChange() {
        // When
        editAnnouncementViewModel.onContentChange(content)

        // Then
        assertEquals(content, editAnnouncementViewModel.uiState.value.content)
    }

    @Test
    fun updateAnnouncement_should_call_updateAnnouncementUseCase() {
        // Given
        editAnnouncementViewModel.onTitleChange(title)
        editAnnouncementViewModel.onContentChange(content)

        // When
        editAnnouncementViewModel.updateAnnouncement()

        // Then
        coVerify { updateAnnouncementUseCase(any()) }
        assertEquals(title.trim(), editAnnouncementViewModel.uiState.value.title)
        assertEquals(content.trim(), editAnnouncementViewModel.uiState.value.content)
    }
}