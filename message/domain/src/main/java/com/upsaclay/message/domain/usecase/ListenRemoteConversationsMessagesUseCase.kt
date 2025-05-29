package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ListenRemoteConversationsMessagesUseCase(
    private val userRepository: UserRepository,
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val scope: CoroutineScope
) {
    internal var job: Job? = null
    private val messageListeningJobs = mutableMapOf<String, MessageListeningJob>()

    fun start() {
        job?.cancel()
        job = scope.launch {
            userRepository.user
                .filterNotNull()
                .collectLatest { user ->
                    conversationRepository.fetchRemoteConversations(user.id)
                        .catch { e("Failed to fetch conversations", it) }
                        .collect { conversation ->
                            if (messageListeningJobs[conversation.id]?.conversation != conversation) {
                                conversationRepository.upsertLocalConversation(conversation)
                                updateMessageListeningJobs(conversation)
                            }
                        }
                }
        }
    }

    fun stop() {
        val keys = messageListeningJobs.keys.toList()
        keys.forEach { conversationId ->
            messageListeningJobs[conversationId]?.job?.cancel()
            messageListeningJobs.remove(conversationId)
        }
        job?.cancel()
    }

    private fun updateMessageListeningJobs(conversation: Conversation) {
        messageListeningJobs[conversation.id]?.job?.cancel()
        val newMessageJob = scope.launch {
            listenRemoteMessages(conversation)
        }
        messageListeningJobs[conversation.id] = MessageListeningJob(conversation, newMessageJob)
    }

    private suspend fun listenRemoteMessages(conversation: Conversation) {
        messageRepository.fetchRemoteMessages(conversation.id, conversation.deleteTime)
            .catch { e("Failed to fetch remote message with ${conversation.interlocutor.fullName}", it) }
            .collect {
                messageRepository.upsertLocalMessage(it)
            }
    }
}

private data class MessageListeningJob(
    val conversation: Conversation,
    val job: Job
)