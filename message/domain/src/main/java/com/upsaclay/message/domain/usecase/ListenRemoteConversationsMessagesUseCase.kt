package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.d
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import kotlin.collections.set

class ListenRemoteConversationsMessagesUseCase(
    private val userRepository: UserRepository,
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val scope: CoroutineScope
) {
    internal var job: Job? = null
    private val listenMessagesJobs = mutableMapOf<String, ListenMessagesJob>()

    fun start() {
        job?.cancel()
        job = scope.launch {
            userRepository.user
                .filterNotNull()
                .collectLatest { user ->
                    conversationRepository.fetchRemoteConversations(user.id)
                        .retryWhen { cause, _ ->
                            d("Retrying listen remote conversations after error: $cause")
                            delay(3000)
                            true
                        }
                        .collect { conversation ->
                            if (listenMessagesJobs[conversation.id]?.conversation != conversation) {
                                launchListenJob(conversation)
                            }
                        }
                }
        }
    }

    fun stop() {
        val keys = listenMessagesJobs.keys.toList()
        keys.forEach { conversationId ->
            listenMessagesJobs[conversationId]?.job?.cancel()
            listenMessagesJobs.remove(conversationId)
        }
        job?.cancel()
    }

    private suspend fun listenRemoteMessages(conversation: Conversation) {
        messageRepository.getRemoteMessages(conversation.id, conversation.deleteTime)
            .retryWhen { cause, _ ->
                d("Retrying listen remote message after error: $cause")
                delay(3000)
                true
            }
            .catch { e("Failed to fetch remote message with ${conversation.interlocutor.fullName}", it) }
            .collect { messages ->
                messages.forEach {
                    messageRepository.upsertLocalMessage(it)
                }
            }
    }

    private suspend fun launchListenJob(conversation: Conversation) {
        conversationRepository.upsertLocalConversation(conversation)
        listenMessagesJobs[conversation.id]?.job?.cancel()
        val newMessageJob = scope.launch {
            listenRemoteMessages(conversation)
        }
        listenMessagesJobs[conversation.id] = ListenMessagesJob(conversation, newMessageJob)
    }
}

private data class ListenMessagesJob(
    val conversation: Conversation,
    val job: Job
)