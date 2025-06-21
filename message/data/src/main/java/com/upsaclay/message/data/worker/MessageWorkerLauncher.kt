package com.upsaclay.message.data.worker

import android.content.Context
import com.upsaclay.common.data.WorkerLauncher

class MessageWorkerLauncher(
    context: Context,
    private val messageWorkerBuilder: MessageWorkerBuilder
): WorkerLauncher(context) {
    override fun launch() {
        workerManager.enqueue(messageWorkerBuilder.buildSynchronizeMessageWorkerRequest())
        workerManager.enqueue(messageWorkerBuilder.buildSynchronizeConversationWorkerRequest())
    }
}