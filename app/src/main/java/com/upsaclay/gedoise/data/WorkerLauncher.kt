package com.upsaclay.gedoise.data

import android.content.Context
import androidx.work.WorkManager
import com.upsaclay.message.data.worker.MessageWorkerFactory

class WorkerLauncher(context: Context) {
    private val workerManager = WorkManager.getInstance(context)
    private val messageWorkerFactory = MessageWorkerFactory()

    fun launch() {
        workerManager.enqueue(messageWorkerFactory.getSynchronizeMessageWorkerRequest())
    }
}