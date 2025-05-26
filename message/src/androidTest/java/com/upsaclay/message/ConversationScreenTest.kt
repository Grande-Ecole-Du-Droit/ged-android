package com.upsaclay.message

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.message.domain.conversationUiFixture
import com.upsaclay.message.domain.conversationsUIFixture
import com.upsaclay.message.domain.messageFixture
import com.upsaclay.message.presentation.conversation.ConversationScreenRoute
import com.upsaclay.message.presentation.conversation.ConversationViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ConversationScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val conversationViewModel: ConversationViewModel = mockk()
    private val uiState = ConversationViewModel.ConversationUiState(
        conversations = conversationsUIFixture
    )

    @Before
    fun setUp() {
        every { conversationViewModel.uiState } returns MutableStateFlow(uiState)
        every { conversationViewModel.event } returns MutableSharedFlow()
    }

    @Test
    fun conversations_should_be_displayed_when_no_empty() {
        // When
        rule.setContent {
            ConversationScreenRoute(
                onConversationClick = {},
                onCreateConversation = {},
                bottomBar = {},
                viewModel = conversationViewModel
            )
        }

        // Then
        rule.onAllNodesWithTag(rule.activity.getString(R.string.conversation_screen_conversation_item_tag))
            .apply {
                fetchSemanticsNodes().forEachIndexed { i, _ ->
                    get(i).assert(hasText(conversationsUIFixture[i].interlocutor.fullName))
                }
            }
    }

    @Test
    fun empty_conversations_text_should_be_displayed_when_no_conversations() {
        // Given
        every { conversationViewModel.uiState } returns MutableStateFlow(
            uiState.copy(conversations = emptyList())
        )

        // When
        rule.setContent {
            ConversationScreenRoute(
                onConversationClick = {},
                onCreateConversation = {},
                bottomBar = {},
                viewModel = conversationViewModel
            )
        }

        // Then
        rule.onNodeWithTag(
            rule.activity.getString(R.string.conversation_screen_empty_conversation_text_tag),
            useUnmergedTree = true
        ).assertExists()
    }

    @Test
    fun read_conversations_item_should_be_displayed_when_last_message_is_read() {
        // Given
        every { conversationViewModel.uiState } returns
                MutableStateFlow(uiState.copy(conversations = listOf(conversationUiFixture)))

        // When
        rule.setContent {
            ConversationScreenRoute(
                onConversationClick = {},
                onCreateConversation = {},
                bottomBar = {},
                viewModel = conversationViewModel
            )
        }

        // Then
        rule.onNodeWithTag(
            rule.activity.getString(R.string.conversation_screen_read_conversation_item_tag),
            useUnmergedTree = true
        ).assertExists()
    }

    @Test
    fun unread_conversations_item_should_be_displayed_when_last_message_is_not_read_and_not_sent_by_interlocutor() {
        // Given
        val lastMessage = messageFixture.copy(seen = false, senderId = userFixture2.id)
        every { conversationViewModel.uiState } returns
                MutableStateFlow(
                    uiState.copy(
                        conversations = listOf(conversationUiFixture.copy(lastMessage = lastMessage))
                    )
                )

        // When
        rule.setContent {
            ConversationScreenRoute(
                onConversationClick = {},
                onCreateConversation = {},
                bottomBar = {},
                viewModel = conversationViewModel
            )
        }

        // Then
        rule.onNodeWithTag(
            rule.activity.getString(R.string.conversation_screen_unread_conversation_item_tag),
            useUnmergedTree = true
        ).assertExists()
    }
}