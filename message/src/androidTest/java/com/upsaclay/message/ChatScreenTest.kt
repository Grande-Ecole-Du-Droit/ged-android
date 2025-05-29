package com.upsaclay.message

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.paging.PagingData
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.messageFixture
import com.upsaclay.message.domain.messageFixture2
import com.upsaclay.message.presentation.chat.ChatScreenRoute
import com.upsaclay.message.presentation.chat.ChatViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val chatViewModel: ChatViewModel = mockk()
    private val uiState = ChatViewModel.ChatUiState(
        conversation = conversationFixture,
        text = "",
    )

    @Before
    fun setUp() {
        every { chatViewModel.uiState } returns MutableStateFlow(uiState)
        every { chatViewModel.event } returns MutableSharedFlow()
        every { chatViewModel.sendMessage() } returns Unit
    }

    @Test
    fun sentMessageItem_should_be_displayed_when_user_is_sender() {
        // Given
        every { chatViewModel.messages } returns MutableStateFlow(PagingData.from(listOf(messageFixture)))

        // When
        rule.setContent {
            ChatScreenRoute(
                conversation = conversationFixture,
                onBackClick = {},
                viewModel = chatViewModel
            )
        }

        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.chat_screen_send_message_item_tag) + 0)
            .assertExists()

    }

    @Test
    fun receiveMessageItem_should_be_displayed_user_is_sender() {
        // Given
        every { chatViewModel.messages } returns MutableStateFlow(PagingData.from(listOf(messageFixture2)))

        // When
        rule.setContent {
            ChatScreenRoute(
                conversation = conversationFixture,
                onBackClick = {},
                viewModel = chatViewModel
            )
        }

        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.chat_screen_receive_message_item_tag) + 0)
            .assertExists()

    }
}