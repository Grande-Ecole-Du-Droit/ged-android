package com.upsaclay.message.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import org.koin.java.KoinJavaComponent.inject

internal class SynchronizeConversationWorker (
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {
    private val conversationRepository: ConversationRepository by inject(ConversationRepository::class.java)
    private val userRepository: UserRepository by inject(UserRepository::class.java)

    override suspend fun doWork(): Result {
        return try {
            val userId = userRepository.getCurrentUser()?.id ?: return Result.failure()
            conversationRepository.getUnCreateConversations().forEach { conversation ->
                conversationRepository.createRemoteConversation(conversation, userId)
                conversationRepository.updateLocalConversation(conversation.copy(state = ConversationState.CREATED))
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}