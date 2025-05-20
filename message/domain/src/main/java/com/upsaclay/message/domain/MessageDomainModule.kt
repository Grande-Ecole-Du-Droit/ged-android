package com.upsaclay.message.domain

import com.upsaclay.common.domain.e
import com.upsaclay.message.domain.usecase.CreateConversationUseCase
import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
import com.upsaclay.message.domain.usecase.GetUnreadMessagesUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteConversationsMessagesUseCase
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val BACKGROUND_SCOPE = named("BackgroundScope")

val messageDomainModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
    SupervisorJob() +
            Dispatchers.IO +
            CoroutineExceptionHandler { _, throwable ->
                e("Uncaught error in backgroundScope", throwable)
            }
        )
    }

    singleOf(::CreateConversationUseCase)
    singleOf(::DeleteConversationUseCase)
    singleOf(::GetUnreadMessagesUseCase)
    singleOf(::SendMessageUseCase)

    single {
        ListenRemoteConversationsMessagesUseCase(
            userRepository = get(),
            conversationRepository = get(),
            messageRepository = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
}
