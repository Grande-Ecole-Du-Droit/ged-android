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
    fun updateAnnouncement_should_updateAnnouncement() {
        // Given
        editAnnouncementViewModel.onTitleChange(title)
        editAnnouncementViewModel.onContentChange(content)

        // When
        editAnnouncementViewModel.updateAnnouncement()

        // Then
        coVerify { updateAnnouncementUseCase(any()) }
    }

    @Test
    fun updateAnnouncement_should_not_update_when_content_is_empty() {
        // Given
        editAnnouncementViewModel.onTitleChange("title")
        editAnnouncementViewModel.onContentChange("")

        // When
        editAnnouncementViewModel.updateAnnouncement()

        // Then
        coVerify(exactly = 0) { updateAnnouncementUseCase(any()) }
    }

    @Test
    fun updateAnnouncement_should_not_update_when_title_and_content_are_same() {
        // Given
        editAnnouncementViewModel.onTitleChange(announcementFixture.title!!)
        editAnnouncementViewModel.onContentChange(announcementFixture.content)

        // When
        editAnnouncementViewModel.updateAnnouncement()

        // Then
        coVerify(exactly = 0) { updateAnnouncementUseCase(any()) }
    }

    @Test
    fun announcement_should_be_trim_when_updated() {
        // Given
        val titleWithSpaces = "  ${announcementFixture.title}  "
        val contentWithSpaces = "  ${announcementFixture.content}  "
        editAnnouncementViewModel.onTitleChange(titleWithSpaces)
        editAnnouncementViewModel.onContentChange(contentWithSpaces)

        // When
        editAnnouncementViewModel.updateAnnouncement()

        // Then
        coVerify {
            updateAnnouncementUseCase(
                announcementFixture.copy(
                    title = titleWithSpaces.trim(),
                    content = contentWithSpaces.trim())
            )
        }
    }
}