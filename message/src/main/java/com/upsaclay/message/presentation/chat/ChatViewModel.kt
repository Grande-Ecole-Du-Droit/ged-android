package com.upsaclay.message.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.NotificationUseCase
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.GetUnreadMessagesUseCase
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

class ChatViewModel(
    private val conversation: Conversation,
    userRepository: UserRepository,
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val sendMessageUseCase: SendMessageUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val getUnreadMessagesUseCase: GetUnreadMessagesUseCase
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
        clearMessageNotifications()
        seeMessages()
        listenConversation()
        emitNewMessageReceived()
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
            sendMessageUseCase(conversation, user, text)
            _uiState.update { it.copy(text = "") }
        } catch (_: IllegalArgumentException) {
            viewModelScope.launch {
                _event.emit(SingleUiEvent.Error(com.upsaclay.common.R.string.user_not_found))
            }
        }
    }

    private fun seeMessages() {
        viewModelScope.launch {
            getUnreadMessagesUseCase()
                .distinctUntilChanged()
                .onEach { delay(50) }
                .collectLatest { messages ->
                    messages
                        .filter { it.senderId != user?.id }
                        .map { messageRepository.updateSeenMessage(it.copy(seen = true)) }
                }
        }
    }

    private fun emitNewMessageReceived() {
        viewModelScope.launch {
            messageRepository.getLastMessage(conversation.id)
                .filter { it.senderId != user?.id }
                .filter { Duration.between(it.date, LocalDateTime.now()).toMinutes() < 1L }
                .collect {
                    _event.emit(MessageEvent.NewMessage(it))
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

    private fun clearMessageNotifications() {
        viewModelScope.launch {
            notificationUseCase.clearNotifications(conversation.id)
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