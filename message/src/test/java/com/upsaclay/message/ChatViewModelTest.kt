package com.upsaclay.message

import androidx.paging.PagingData
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.messageFixture
import com.upsaclay.message.domain.messagesFixture
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.MessageNotificationUseCase
import com.upsaclay.message.domain.usecase.ResendMessageUseCase
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import com.upsaclay.message.presentation.chat.ChatViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {
    private val userRepository: UserRepository = mockk()
    private val conversationRepository: ConversationRepository = mockk()
    private val messageRepository: MessageRepository = mockk()
    private val sendMessageUseCase: SendMessageUseCase = mockk()
    private val resendMessageUseCase: ResendMessageUseCase = mockk()
    private val messageNotificationUseCase: MessageNotificationUseCase = mockk()

    private lateinit var chatViewModel: ChatViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { conversationRepository.getConversationFlow(any()) } returns flowOf(conversationFixture)
        every { userRepository.user } returns MutableStateFlow(userFixture)
        every { userRepository.currentUser } returns userFixture
        every { messageRepository.getPagingMessages(any()) } returns flowOf(PagingData.from(messagesFixture))
        every { messageRepository.getLastMessageFlow(any()) } returns flowOf(messageFixture)
        every { sendMessageUseCase(any(), any(), any()) } returns Unit
        coEvery { resendMessageUseCase(any()) } returns Unit
        coEvery { messageRepository.updateSeenMessages(any(), any()) } returns Unit
        coEvery { messageNotificationUseCase.clearNotifications(any()) } returns Unit
        coEvery { messageNotificationUseCase.sendNotification(any()) } returns Unit

        chatViewModel = ChatViewModel(
            conversation = conversationFixture,
            userRepository = userRepository,
            conversationRepository = conversationRepository,
            messageRepository = messageRepository,
            sendMessageUseCase = sendMessageUseCase,
            resendMessageUseCase = resendMessageUseCase,
            messageNotificationUseCase = messageNotificationUseCase,
        )
    }

    @Test
    fun update_text_to_send_should_update_text_to_send() {
        // Given
        val text = "Hello"

        // When
        chatViewModel.onTextChange(text)

        // Then
        assertEquals(text, chatViewModel.uiState.value.text)
    }

    @Test
    fun send_message_should_send_message() {
        // Given
        chatViewModel.onTextChange("Hello")

        // When
        chatViewModel.sendMessage()

        // Then
        coVerify { sendMessageUseCase(any(), any(), any()) }
    }

    @Test
    fun send_message_should_reset_text_to_send() {
        // Given
        chatViewModel.onTextChange("Hello")

        // When
        chatViewModel.sendMessage()

        // Then
        assertEquals("", chatViewModel.uiState.value.text)
    }
}