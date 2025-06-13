package com.upsaclay.message.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.MessageNotificationUseCase
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

class ChatViewModel(
    private val conversation: Conversation,
    userRepository: UserRepository,
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val sendMessageUseCase: SendMessageUseCase,
    private val messageNotificationUseCase: MessageNotificationUseCase,
): ViewModel() {
    private val user: User? = userRepository.currentUser
    private val _uiState = MutableStateFlow(
        ChatUiState(
            conversation = conversation,
            text = ""
        )
    )
    internal val uiState: StateFlow<ChatUiState> = _uiState
    internal val messages: Flow<PagingData<Message>> = messageRepository.getPagingMessages(conversation.id)
    private val _event = MutableSharedFlow<SingleUiEvent>()
    internal val event: Flow<SingleUiEvent> = _event

    init {
        listenConversation()
        emitNewMessageReceived()
        seeMessage()
        seeNewMessage()
        clearChatNotifications()
    }

    fun onTextChange(text: String) {
        _uiState.update {
            it.copy(text = text)
        }
    }

    fun sendMessage() {
        try {
            val text = _uiState.value.text.takeUnless { it.isEmpty() } ?: return
            val conversation = _uiState.value.conversation
            val user = requireNotNull(user)
            val message = Message(
                id = GenerateIdUseCase.longId,
                conversationId = conversation.id,
                senderId = user.id,
                recipientId = conversation.interlocutor.id,
                content = text,
                date = LocalDateTime.now(ZoneOffset.UTC),
                state = MessageState.DRAFT
            )
            sendMessageUseCase(message, conversation, user.id)
            _uiState.update { it.copy(text = "") }
        } catch (_: IllegalArgumentException) {
            viewModelScope.launch {
                _event.emit(SingleUiEvent.Error(com.upsaclay.common.R.string.user_not_found))
            }
        }
    }

    fun seeMessage() {
        viewModelScope.launch {
            user?.let {
                messageRepository.updateSeenMessages(conversation.id, it.id)
            }
        }
    }

    private fun emitNewMessageReceived() {
        viewModelScope.launch {
            messageRepository.getLastMessageFlow(conversation.id)
                .filterNotNull()
                .filter { it.senderId != user?.id }
                .filter { Duration.between(it.date, LocalDateTime.now(ZoneOffset.UTC)).toMinutes() < 1L }
                .collect {
                    _event.emit(MessageEvent.NewMessage(it))
                }
        }
    }

    private fun seeNewMessage() {
        viewModelScope.launch {
            messageRepository.getLastMessageFlow(conversation.id)
                .filterNotNull()
                .filter { it.senderId != user?.id }
                .collect {
                    messageRepository.updateSeenMessage(it)
                }
        }
    }

    private fun listenConversation() {
        viewModelScope.launch {
            conversationRepository.getConversationFlow(conversation.interlocutor.id)
                .collect { conversation ->
                    _uiState.update { it.copy(conversation = conversation) }
                }
        }
    }

    private fun clearChatNotifications() {
        viewModelScope.launch {
            messageNotificationUseCase.clearNotifications(_uiState.value.conversation.id)
        }
    }

    internal data class ChatUiState(
        val conversation: Conversation,
        val text: String
    )

    internal sealed class MessageEvent: SingleUiEvent {
        data class NewMessage(val message: Message): MessageEvent()
    }
}