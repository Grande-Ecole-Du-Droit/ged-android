package com.upsaclay.gedoise.domain.usecase

import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase

class ListenDataUseCase(
    private val listenRemoteConversationsUseCase: ListenRemoteConversationsUseCase,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase
) {
    fun start() {
        listenRemoteConversationsUseCase.start()
        listenRemoteMessagesUseCase.start()
        listenRemoteUserUseCase.start()
    }

    fun stop() {
        listenRemoteConversationsUseCase.stop()
        listenRemoteMessagesUseCase.stop()
        listenRemoteUserUseCase.stop()
    }
}