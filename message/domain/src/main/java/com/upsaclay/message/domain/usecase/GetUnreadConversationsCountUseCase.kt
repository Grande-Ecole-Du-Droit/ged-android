package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.repository.ConversationMessageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class GetUnreadConversationsCountUseCase(
    private val conversationMessageRepository: ConversationMessageRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Int> = userRepository.user.flatMapLatest { user ->
        conversationMessageRepository.conversationsMessage
            .mapLatest { conversationsMessage ->
                conversationsMessage.count {
                    it.lastMessage.senderId != user.id && !it.lastMessage.seen
                }
            }
        }
}