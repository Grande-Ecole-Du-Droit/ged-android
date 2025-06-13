package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.e
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ListenRemoteMessagesUseCase(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val scope: CoroutineScope
) {
    internal var job: Job? = null
    internal var fetchedConversations = mutableMapOf<String, Conversation>()

    fun start() {
        job?.cancel()
        job = scope.launch {
            getConversationsFlow()
                .collect { conversations ->
                    listenRemoteMessages(conversations)
                }
        }
    }

    fun stop() {
        job?.cancel()
        fetchedConversations.clear()
    }

    internal fun getConversationsFlow(): Flow<List<Conversation>> {
        return conversationRepository.getConversationsFlow()
            .map {
                it.filter { conversation ->
                    if (fetchedConversations.contains(conversation.id)) {
                        false
                    } else {
                        fetchedConversations[conversation.id] = conversation
                        true
                    }
                }
            }
    }

    internal suspend fun listenRemoteMessages(conversations: List<Conversation>) {
        conversations.forEach { conversation ->
            val offsetTime = messageRepository.getLastMessage(conversation.id)?.date
            messageRepository.fetchRemoteMessages(conversation.id, conversation.interlocutor.id, offsetTime)
                .catch { error ->
                    e("Failed to fetch remote message with ${conversation.interlocutor.fullName}", error)
                }
                .collect { message ->
                    messageRepository.upsertLocalMessage(message)
                }
        }
    }
}