package com.upsaclay.message.data.worker

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder

class MessageWorkerBuilder {
    fun buildSynchronizeMessageWorkerRequest(): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return OneTimeWorkRequestBuilder<SynchronizeMessageWorker>()
            .setConstraints(constraints)
            .build()
    }

    fun buildSynchronizeConversationWorkerRequest(): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return OneTimeWorkRequestBuilder<SynchronizeConversationWorker>()
            .setConstraints(constraints)
            .build()
    }
}