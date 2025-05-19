package com.upsaclay.gedoise.domain.usecase

import com.upsaclay.message.domain.usecase.ListenRemoteConversationsMessagesUseCase

class DataListeningUseCase(
    private val listenRemoteConversationsMessagesUseCase: ListenRemoteConversationsMessagesUseCase,
) {
    fun start() {
        listenRemoteConversationsMessagesUseCase.start()
    }

    fun stop() {
        listenRemoteConversationsMessagesUseCase.stop()
    }
}