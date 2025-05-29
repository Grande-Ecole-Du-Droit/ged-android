package com.upsaclay.news

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.upsaclay.news.domain.announcementFixture
import com.upsaclay.news.presentation.announcement.editannouncement.EditAnnouncementDestination
import com.upsaclay.news.presentation.announcement.editannouncement.EditAnnouncementViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditAnnouncementScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val editAnnouncementViewModel: EditAnnouncementViewModel = mockk()
    private val uiState = EditAnnouncementViewModel.EditAnnouncementUiState(
        title = "",
        content = ""
    )

    @Before
    fun setUp() {
        every { editAnnouncementViewModel.uiState } returns MutableStateFlow(uiState)
        every { editAnnouncementViewModel.event } returns MutableSharedFlow()
    }

    @Test
    fun save_button_should_be_disabled_when_updateEnabled_is_false() {
        // Given
        every { editAnnouncementViewModel.uiState } returns MutableStateFlow(uiState.copy(updateEnabled = false))

        // When
        rule.setContent {
            EditAnnouncementDestination(
                announcement = announcementFixture,
                onBackClick = {},
                viewModel = editAnnouncementViewModel
            )
        }

        // Then
        rule.onNodeWithText(rule.activity.getString(com.upsaclay.common.R.string.save))
            .assert(isNotEnabled())
    }

    @Test
    fun save_button_should_be_enabled_when_updateEnabled_is_true() {
        // Given
        every { editAnnouncementViewModel.uiState } returns MutableStateFlow(uiState.copy(content = ""))

        // When
        rule.setContent {
            EditAnnouncementDestination(
                announcement = announcementFixture,
                onBackClick = {},
                viewModel = editAnnouncementViewModel
            )
        }

        // Then
        rule.onNodeWithText(rule.activity.getString(com.upsaclay.common.R.string.save))
            .assert(isNotEnabled())
    }
}