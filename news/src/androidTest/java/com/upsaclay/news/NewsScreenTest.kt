package com.upsaclay.news

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.announcementFixture
import com.upsaclay.news.domain.announcementsFixture
import com.upsaclay.news.presentation.news.NewsDestination
import com.upsaclay.news.presentation.news.NewsViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val newsViewModel: NewsViewModel = mockk()
    private val uiState = NewsViewModel.NewsUiState(
        user = userFixture,
        announcements = announcementsFixture,
        refreshing = false
    )

    @Before
    fun setUp() {
        every { newsViewModel.event } returns MutableSharedFlow()
        every { newsViewModel.uiState } returns MutableStateFlow(uiState)
        coEvery { newsViewModel.refreshAnnouncements() } returns Unit
    }

    @Test
    fun empty_announcements_show_empty_announcement_text() {
        // Given
        every { newsViewModel.uiState } returns MutableStateFlow(uiState.copy(announcements = emptyList()))

        // When
        rule.setContent {
            NewsDestination(
                onAnnouncementClick = {},
                onCreateAnnouncementClick = {},
                onProfilePictureClick = {},
                bottomBar = {},
                viewModel = newsViewModel
            )
        }

        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.news_screen_empty_announcement_text_tag))
            .assertExists()
    }

    @Test
    fun no_empty_announcements_show_announcements() {
        // When
        rule.setContent {
            NewsDestination(
                onAnnouncementClick = {},
                onCreateAnnouncementClick = {},
                onProfilePictureClick = {},
                bottomBar = {},
                viewModel = newsViewModel
            )
        }

        // Then
        rule.onAllNodesWithTag(rule.activity.getString(R.string.news_screen_recent_announcements_tag))
            .apply {
                fetchSemanticsNodes().forEachIndexed { i, _ ->
                    get(i).assertExists()
                }
            }
    }

    @Test
    fun announcements_with_title_show_announcements_with_title() {
        // When
        rule.setContent {
            NewsDestination(
                onAnnouncementClick = {},
                onCreateAnnouncementClick = {},
                onProfilePictureClick = {},
                bottomBar = {},
                viewModel = newsViewModel
            )
        }

        // Then
        rule.onAllNodesWithTag(rule.activity.getString(R.string.news_screen_recent_announcements_tag))
            .apply {
                fetchSemanticsNodes().forEachIndexed { i, _ ->
                    get(i).assert(hasText(announcementFixture.title!!))
                }
            }
    }

    @Test
    fun announcements_without_title_show_announcements_with_content() {
        // Given
        val content = announcementFixture.content.take(100)
        val announcement = announcementFixture.copy(title = null, content = content)
        every { newsViewModel.uiState } returns MutableStateFlow(uiState.copy(announcements = listOf(announcement)))

        // When
        rule.setContent {
            NewsDestination(
                onAnnouncementClick = {},
                onCreateAnnouncementClick = {},
                onProfilePictureClick = {},
                bottomBar = {},
                viewModel = newsViewModel
            )
        }

        // Then
        rule.onAllNodesWithTag(rule.activity.getString(R.string.news_screen_recent_announcements_tag))
            .apply {
                fetchSemanticsNodes().forEachIndexed { i, _ ->
                    get(i).assert(hasText(content))
                }
            }
    }
}