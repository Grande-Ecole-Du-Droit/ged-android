package com.upsaclay.message.data.repository

import com.upsaclay.common.data.exceptions.handleNetworkException
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.data.local.ConversationLocalDataSource
import com.upsaclay.message.data.mapper.toConversation
import com.upsaclay.message.data.remote.ConversationRemoteDataSource
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
internal class ConversationRepositoryImpl(
    private val userRepository: UserRepository,
    private val conversationLocalDataSource: ConversationLocalDataSource,
    private val conversationRemoteDataSource: ConversationRemoteDataSource,
) : ConversationRepository {
    private val interlocutors = mutableMapOf<String, User>()

    override fun getLocalConversationFlow(interlocutorId: String): Flow<Conversation> =
        conversationLocalDataSource.getFlowLocalConversation(interlocutorId).filterNotNull()

    override suspend fun getLocalConversation(interlocutorId: String): Conversation? =
        conversationLocalDataSource.getConversation(interlocutorId)

    override suspend fun getRemoteConversations(userId: String): Flow<Conversation> {
        return conversationRemoteDataSource.listenConversations(userId)
            .flatMapMerge { remoteConversation ->
                val interlocutorId = remoteConversation.participants.firstOrNull { it != userId }
                    ?: return@flatMapMerge emptyFlow()

                interlocutors[interlocutorId]?.let {
                    flowOf(remoteConversation.toConversation(userId, it))
                } ?: run {
                    userRepository.getUserFlow(interlocutorId).filterNotNull().map {
                        remoteConversation.toConversation(userId, it)
                    }
                }
            }
    }

    override suspend fun createConversation(conversation: Conversation, userId: String) {
        handleNetworkException(
            message = "Failed to create conversation",
            block = {
                conversationRemoteDataSource.createConversation(conversation, userId)
                conversationLocalDataSource.upsertConversation(conversation)
            }
        )
    }

    override suspend fun upsertLocalConversation(conversation: Conversation) {
        conversationLocalDataSource.upsertConversation(conversation)
    }

    override suspend fun deleteConversation(conversation: Conversation, userId: String) {
        val deleteTime = LocalDateTime.now()
        handleNetworkException(
            message = "Failed to delete conversation",
            block = {
                conversationRemoteDataSource.updateConversationDeleteTime(conversation.id, userId, deleteTime)
                conversationLocalDataSource.deleteConversation(conversation)
            }
        )
    }

    override suspend fun deleteLocalConversations() {
        conversationLocalDataSource.deleteConversations()
    }
}