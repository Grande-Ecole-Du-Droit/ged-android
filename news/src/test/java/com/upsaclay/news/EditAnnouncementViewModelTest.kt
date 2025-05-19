package com.upsaclay.news

import com.upsaclay.news.domain.announcementFixture
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.presentation.announcement.edit.EditAnnouncementViewModel
import io.mockk.coEvery
import io.mockk.every
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
    private val announcementRepository: AnnouncementRepository = mockk()

    private lateinit var editAnnouncementViewModel: EditAnnouncementViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val title = "Title"
    private val content = "Content"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { announcementRepository.getAnnouncement(announcementFixture.id) } returns announcementFixture
        coEvery { announcementRepository.updateAnnouncement(announcementFixture) } returns Unit

        editAnnouncementViewModel = EditAnnouncementViewModel(
            announcementId = announcementFixture.id,
            announcementRepository = announcementRepository
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
}