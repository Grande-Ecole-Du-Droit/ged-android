package com.upsaclay.gedoise.domain.usecase

import com.upsaclay.message.domain.usecase.ListenRemoteConversationsMessagesUseCase

class DataListeningUseCase(
    private val listenRemoteConversationsMessagesUseCase: ListenRemoteConversationsMessagesUseCase,
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase
) {
    fun start() {
        listenRemoteConversationsMessagesUseCase.start()
        listenRemoteUserUseCase.start()
    }

    fun stop() {
        listenRemoteConversationsMessagesUseCase.stop()
        listenRemoteUserUseCase.stop()
    }
}