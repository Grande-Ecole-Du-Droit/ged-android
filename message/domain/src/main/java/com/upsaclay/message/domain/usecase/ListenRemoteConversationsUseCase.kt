package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListenRemoteConversationsUseCase(
    private val userRepository: UserRepository,
    private val conversationRepository: ConversationRepository,
    private val scope: CoroutineScope
) {
    private var job: Job? = null

    fun start() {
        job?.cancel()
        job = scope.launch {
            userRepository.user
                .collectLatest { user ->
                    conversationRepository.fetchRemoteConversations(user.id)
                        .catch { e("Failed to fetch conversations", it) }
                        .collect { conversation ->
                            conversationRepository.upsertLocalConversation(conversation)
                        }
                }
        }
    }

    fun stop() {
        job?.cancel()
    }
}