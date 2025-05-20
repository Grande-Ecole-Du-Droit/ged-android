package com.upsaclay.news

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.upsaclay.news.presentation.announcement.create.CreateAnnouncementDestination
import com.upsaclay.news.presentation.announcement.create.CreateAnnouncementViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateAnnouncementScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val createAnnouncementViewModel: CreateAnnouncementViewModel = mockk()
    private val uiState = CreateAnnouncementViewModel.CreateAnnouncementUiState(
        title = "",
        content = ""
    )

    @Before
    fun setUp() {
        every { createAnnouncementViewModel.uiState } returns MutableStateFlow(uiState)
        every { createAnnouncementViewModel.createAnnouncement() } returns Unit
        every { createAnnouncementViewModel.onTitleChange(any()) } returns Unit
        every { createAnnouncementViewModel.onContentChange(any()) } returns Unit
    }

    @Test
    fun post_button_should_be_disabled_only_when_content_is_empty() {
        // Given
        every { createAnnouncementViewModel.uiState } returns MutableStateFlow(uiState.copy(title = "title"))

        // When
        rule.setContent {
            CreateAnnouncementDestination(
                onBackClick = {},
                viewModel = createAnnouncementViewModel
            )
        }

        // Then
        rule.onNodeWithText(rule.activity.getString(com.upsaclay.common.R.string.publish))
            .assert(isNotEnabled())

    }

    @Test
    fun post_button_should_be_disabled_only_when_content_is_not_empty() {
        // Given
        every { createAnnouncementViewModel.uiState } returns MutableStateFlow(uiState.copy(content = "content"))

        // When
        rule.setContent {
            CreateAnnouncementDestination(
                onBackClick = {},
                viewModel = createAnnouncementViewModel
            )
        }

        // Then
        rule.onNodeWithText(rule.activity.getString(com.upsaclay.common.R.string.publish))
            .assert(isEnabled())
    }
}