package com.upsaclay.news

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.news.domain.announcementFixture
import com.upsaclay.news.presentation.announcement.read.ReadAnnouncementScreenRoute
import com.upsaclay.news.presentation.announcement.read.ReadAnnouncementViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReadAnnouncementScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val readAnnouncementViewModel: ReadAnnouncementViewModel = mockk()
    private val uiState = ReadAnnouncementViewModel.ReadAnnouncementUiState(
        announcement = announcementFixture,
        user = userFixture,
        loading = false
    )

    @Before
    fun setUp() {
        every { readAnnouncementViewModel.uiState } returns MutableStateFlow(uiState)
        every { readAnnouncementViewModel.singleUiEvent } returns MutableSharedFlow()
    }

    @Test
    fun option_button_should_be_displayed_when_user_is_member_and_author() {
        // When
        rule.setContent {
            ReadAnnouncementScreenRoute(
                announcementId = announcementFixture.id,
                onBackClick = {},
                onEditClick = {},
                viewModel = readAnnouncementViewModel
            )
        }

        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.read_screen_option_button_tag)).assertExists()
    }

    @Test
    fun option_button_should_not_be_displayed_when_user_is_not_member() {
        // When
        rule.setContent {
             ReadAnnouncementScreenRoute(
                announcementId = announcementFixture.id,
                onBackClick = {},
                onEditClick = {},
                viewModel = readAnnouncementViewModel
            )
        }

        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.read_screen_sheet_edit_field_tag)).assertDoesNotExist()
    }

    @Test
    fun option_button_should_not_be_displayed_when_user_is_member_and_not_author() {
        // Given
        every { readAnnouncementViewModel.uiState } returns MutableStateFlow(
            uiState.copy(announcement = announcementFixture.copy(author = userFixture2))
        )

        // When
        rule.setContent {
             ReadAnnouncementScreenRoute(
                announcementId = announcementFixture.id,
                onBackClick = {},
                onEditClick = {},
                viewModel = readAnnouncementViewModel
            )
        }

        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.read_screen_sheet_edit_field_tag)).assertDoesNotExist()
    }

    @Test
    fun clicking_option_should_display_bottom_sheet() {
        // When
        rule.setContent {
             ReadAnnouncementScreenRoute(
                announcementId = announcementFixture.id,
                onBackClick = {},
                onEditClick = {},
                viewModel = readAnnouncementViewModel
            )
        }

        rule.onNodeWithTag(rule.activity.getString(R.string.read_screen_option_button_tag)).performClick()

        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.read_screen_bottom_sheet_tag)).assertExists()
    }


    @Test
    fun clicking_delete_sheet_field_should_display_delete_dialog() {
        // When
        rule.setContent {
             ReadAnnouncementScreenRoute(
                announcementId = announcementFixture.id,
                onBackClick = {},
                onEditClick = {},
                viewModel = readAnnouncementViewModel
            )
        }

        rule.onNodeWithTag(rule.activity.getString(R.string.read_screen_option_button_tag)).performClick()
        rule.onNodeWithTag(rule.activity.getString(R.string.read_screen_sheet_delete_field_tag)).performClick()

        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.read_screen_delete_dialog_tag)).assertExists()
    }
}