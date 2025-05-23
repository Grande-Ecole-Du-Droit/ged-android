package com.upsaclay.message.data.repository

import com.google.firebase.Timestamp
import com.upsaclay.common.data.exceptions.handleNetworkException
import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.data.local.ConversationLocalDataSource
import com.upsaclay.message.data.mapper.toConversation
import com.upsaclay.message.data.remote.ConversationRemoteDataSource
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

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


    override suspend fun getRemoteConversationState(conversationId: String, interlocutorId: String): ConversationState? =
        conversationRemoteDataSource.getConversationState(conversationId, interlocutorId)

    override suspend fun fetchRemoteConversations(userId: String): Flow<Conversation> {
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
            .catch { e("Failed to fetch conversations", it) }
    }

    override suspend fun createRemoteConversation(conversation: Conversation, userId: String) {
        handleNetworkException(
            message = "Failed to create conversation",
            block = { conversationRemoteDataSource.createConversation(conversation, userId) }
        )
    }

    override suspend fun upsertLocalConversation(conversation: Conversation) {
        conversationLocalDataSource.upsertConversation(conversation)
    }

    override suspend fun unDeleteRemoteConversation(conversation: Conversation, userId: String) {
        handleNetworkException(
            message = "Failed to undelete conversation",
            block = { conversationRemoteDataSource.unDeleteConversation(conversation, userId) }
        )
    }

    override suspend fun softDeleteConversation(conversation: Conversation, userId: String) {
        val deleteTime = conversation.deleteTime?.toTimestamp() ?: Timestamp.now()
        handleNetworkException(
            message = "Failed to soft delete conversation",
            block = {
                conversationRemoteDataSource.softDeleteConversation(conversation.id, userId, deleteTime)
                conversationLocalDataSource.deleteConversation(conversation)
            }
        )
    }

    override suspend fun hardDeleteConversation(conversationId: String) {
        handleNetworkException(
            message = "Failed to hard delete conversation",
            block = { conversationRemoteDataSource.hardDeleteConversation(conversationId) }
        )
    }

    override suspend fun deleteLocalConversations() {
        conversationLocalDataSource.deleteConversations()
    }
}