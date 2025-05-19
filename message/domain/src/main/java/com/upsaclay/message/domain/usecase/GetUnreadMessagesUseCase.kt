package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.repository.ConversationMessageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class GetUnreadMessagesUseCase(
    private val conversationMessageRepository: ConversationMessageRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<Message>> = userRepository.user
        .filterNotNull()
        .distinctUntilChangedBy { it.id }
        .flatMapLatest { user ->
            conversationMessageRepository.conversationsMessage
                .mapLatest { conversationsMessage ->
                    conversationsMessage
                        .map { it.lastMessage }
                        .filter { it.senderId != user.id  && !it.seen }
                }
        }
}