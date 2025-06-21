package com.upsaclay.message.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.repository.MessageRepository
import org.koin.java.KoinJavaComponent.inject

internal class SynchronizeMessageWorker(
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {
    private val messageRepository: MessageRepository by inject(MessageRepository::class.java)

    override suspend fun doWork(): Result {
        return try {
            messageRepository.getUnsentMessages().forEach { message ->
                messageRepository.createRemoteMessage(message)
                messageRepository.updateLocalMessage(message.copy(state = MessageState.SENT))
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}