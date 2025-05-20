package com.upsaclay.message.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserNotFoundException
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.NotificationUseCase
import com.upsaclay.message.R
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    conversation: Conversation,
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
            messages = emptyList(),
            conversation = conversation,
            text = ""
        )
    )
    internal val uiState: StateFlow<ChatUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    internal val event: Flow<SingleUiEvent> = _event

    init {
        clearMessageNotifications()
        seeMessages()
        listenMessages()
        listenConversation()
        newMessageReceived()
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
            viewModelScope.launch {
                sendMessageUseCase(conversation, user, text)
            }
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

    private fun clearMessageNotifications() {
        viewModelScope.launch {
            notificationUseCase.clearNotifications(_uiState.value.conversation.id)
        }
    }

    private fun listenMessages() {
        viewModelScope.launch {
            messageRepository.getLocalMessages(_uiState.value.conversation.id)
                .collectLatest { messages ->
                    _uiState.update {
                        it.copy(messages = messages)
                    }
                }
        }
    }

    private fun newMessageReceived() {
        viewModelScope.launch {
            uiState
                .map { it.messages }
                .distinctUntilChanged()
                .mapNotNull { it.firstOrNull() }
                .filter { it.senderId != user?.id }
                .collect {
                    _event.emit(MessageEvent.NewMessage(it))
                }
        }
    }

    private fun listenConversation() {
        viewModelScope.launch {
            conversationRepository.getLocalConversationFlow(_uiState.value.conversation.interlocutor.id)
                .collect { conversation ->
                    _uiState.update { it.copy(conversation = conversation) }
                }
        }
    }

    internal data class ChatUiState(
        val messages: List<Message>,
        val conversation: Conversation,
        val text: String
    )

    internal sealed class MessageEvent: SingleUiEvent {
        data class NewMessage(val message: Message): MessageEvent()
    }
}