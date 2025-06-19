package com.upsaclay.message.data.worker

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder

class MessageWorkerFactory {
    fun getSynchronizeMessageWorkerRequest(): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return OneTimeWorkRequestBuilder<SynchronizeMessageWorker>()
            .setConstraints(constraints)
            .build()
    }
}